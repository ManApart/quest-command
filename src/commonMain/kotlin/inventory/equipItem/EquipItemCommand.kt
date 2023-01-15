package inventory.equipItem

import core.Player
import core.body.Body
import core.body.Slot
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

class EquipItemCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Equip", "eq")
    }

    override fun getDescription(): String {
        return "Equip an item from your inventory."
    }

    override fun getManual(): String {
        return """
	Equip <item> - Equip an item
	Equip <item> to <body part> - Equip an item to a specific body part (ex: left hand). X
	Equip <item> to <body part> f - Equip an item even if that means unequipping what's already equipped there. X"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.inventory.getAllItems().map { it.name }
            args.size == 1 -> listOf("to")
            args.last() == "to" -> source.thing.body.getParts().map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "on")))
        val arguments = Args(args, delimiters, flags = listOf("f"))

        if (arguments.isEmpty()) {
            suggestEquippableItems(source)
        } else {
            val item = getItem(source.thing, arguments)
            val attachPointGuess = getAttachPoint(arguments)
            val body = source.body
            val force = arguments.hasFlag("f")

            if (item == null) {
                source.displayToMe("Could not find ${arguments.getBaseString()}. (Did you mean 'equip <item> to <body part>?")
            } else {
                if (!item.canEquipTo(body)) {
                    source.displayToMe("You can't equip ${item.name}.")
                } else {
                    val slot = findSlot(attachPointGuess, body, item)
                    if (slot == null) {
                        suggestAttachPoints(source, attachPointGuess, item)
                    } else {
                        val equippedItems = slot.getEquippedItems(body)
                        if (equippedItems.isNotEmpty() && !force) {
                            confirmEquip(source, item, equippedItems, attachPointGuess)
                        } else {
                            EventManager.postEvent(EquipItemEvent(source.thing, item, slot))
                        }
                    }
                }
            }
        }
    }

    private suspend fun getItem(source: Thing, args: Args): Thing? {
        val itemName = args.getBaseString()
        return source.inventory.getItem(itemName)
    }

    private fun getAttachPoint(args: Args): String? {
        return if (args.hasGroup("to")) {
            args.getString("to")
        } else {
            null
        }
    }

    private suspend fun findSlot(attachPointGuess: String?, body: Body, item: Thing): Slot? {
        return if (attachPointGuess == null) {
            body.getDefaultSlot(item)
        } else {
            item.findSlot(body, attachPointGuess) ?: item.findSlotFromPart(body, attachPointGuess)
        }
    }

    private suspend fun suggestEquippableItems(source: Player) {
        source.respondSuspend("There is nothing you can equip.") {
            message("What do you want to equip?")
            optionsNamed(getEquipableItems(source.thing))
            command { "equip $it" }
        }
    }

    private suspend fun getEquipableItems(source: Thing): List<Thing> {
        val body = source.body
        val equippedItems = body.getEquippedItems()
        return source.inventory.getAllItems().filter { it.canEquipTo(body) && !equippedItems.contains(it) }
    }

    private fun suggestAttachPoints(source: Player, attachPointGuess: String?, item: Thing) {
        val message = "Could not find attach point $attachPointGuess. Where would you like to equip ${item.name}?\n\t${item.equipSlots.joinToString("\n\t")}"
        val response = ResponseRequest(message,
            item.equipSlots.flatMap { it.attachPoints }.associateWith { "equip ${item.name} to $it" })
        CommandParsers.setResponseRequest(source, response)
    }

    private fun confirmEquip(source: Player, newEquip: Thing, equippedItems: List<Thing>, attachPoint: String?) {
        val toPart = if (attachPoint.isNullOrBlank()) "" else " to $attachPoint"
        source.respond({}) {
            message("Replace ${equippedItems.joinToString(", "){it.name}} with ${newEquip.name}?")
            yesNoOptions("equip ${newEquip.name}$toPart f", "")
        }
    }
}