package inventory.equipItem

import core.Player
import core.body.Body2
import core.body.EquipTarget
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

//TODO - equip to layer of part
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
	Equip <item> to <body part> f - Equip an item even if that means unequipping what's already equipped there."""
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
            val partGuess = arguments.getStringIfDelimExists("to")
            val body = source.body2
            val force = arguments.hasFlag("f")

            if (item == null) {
                source.displayToMe("Could not find ${arguments.getBaseString()}. (Did you mean 'equip <item> to <body part>?")
            } else {
                if (!body.canEquip(item)) {
                    source.displayToMe("You can't equip ${item.name}.")
                } else {
                    val target = findTarget(partGuess, body, item)
                    if (target == null) {
                        suggestsEquipPoint(source, partGuess, item)
                    } else {
                        val equippedItems = body.getEquippedAt(target)
                        if (equippedItems.isNotEmpty() && !force) {
                            confirmEquip(source, item, equippedItems, partGuess)
                        } else {
                            EventManager.postEvent(EquipItemEvent(source.thing, item, target))
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

    private fun findTarget(partGuess: String?, body: Body2, item: Thing): EquipTarget? {
        return if (partGuess == null) {
            body.getDefaultTarget(item)
        } else {
            body.findEquipTarget(item, partGuess)
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
        val body = source.body2
        val equippedItems = body.getEquipped()
        return source.inventory.getAllItems().filter { body.canEquip(it) && !equippedItems.contains(it) }
    }

    private fun suggestsEquipPoint(source: Player, partGuess: String?, item: Thing) {
        val message = "Could not find attach point $partGuess. Where would you like to equip ${item.name}?\n\t${item.equipSlots.joinToString("\n\t")}"
        val response = ResponseRequest(message,
            item.equipSlots.flatMap { it.attachPoints }.associateWith { "equip ${item.name} to $it" })
        CommandParsers.setResponseRequest(source, response)
    }

    private fun confirmEquip(source: Player, newEquip: Thing, equippedItems: List<Thing>, partGuess: String?) {
        val toPart = if (partGuess.isNullOrBlank()) "" else " to $partGuess"
        source.respond({}) {
            message("Replace ${equippedItems.joinToString(", "){it.name}} with ${newEquip.name}?")
            yesNoOptions("equip ${newEquip.name}$toPart f", "")
        }
    }
}
