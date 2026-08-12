package inventory.equipItem

import core.Player
import core.body.Body2
import core.body.EquipLayerStrings.ARMOR
import core.body.EquipLayerStrings.CLOTHING
import core.body.EquipLayerStrings.GRIP
import core.body.EquipTarget
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
	Equip <item> on <body part> - Equip an item to a specific body part (ex: left hand).
	Equip <item> to <layer> on <body part> - Equip an item to a specific layer on a body part (ex: to clothing on chest). Used in the case that you could equip something in either a clothing or armor slot
	Equip <item> on <body part> f - Equip an item even if that means unequipping what's already equipped there."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.inventory.getAllItems().map { it.name }
            args.size == 1 -> listOf("on", "to")
            args.last() == "on" -> source.thing.body.getParts().map { it.name }
            args.last() == "to" -> {
                val arguments = args(args, "on", "to")
                val item = getItem(source.thing, arguments)
                item?.let { it.equipTargets.map { t -> t.layer } } ?: listOf(GRIP, CLOTHING, ARMOR)
            }

            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delims("to", "on"), flags = listOf("f"))

        if (arguments.isEmpty()) {
            suggestEquippableItems(source)
        } else {
            val item = getItem(source.thing, arguments)
            val partGuess = arguments.getStringIfDelimExists("on")
            val layerGuess = arguments.getStringIfDelimExists("to")
            val body = source.body2
            val force = arguments.hasFlag("f")

            if (item == null) {
                source.displayToMe("Could not find ${arguments.getBaseString()}. (Did you mean 'equip <item> on <body part>?")
            } else {
                if (!body.canEquip(item)) {
                    source.displayToMe("You can't equip ${item.name}.")
                } else {
                    val target = findTarget(partGuess, body, item, layerGuess)
                    if (target == null) {
                        suggestsEquipPoint(source, item, partGuess)
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
        if (!args.hasBase()) return null
        val itemName = args.getBaseString()
        return source.inventory.getItem(itemName)
    }

    private fun findTarget(partGuess: String?, body: Body2, item: Thing, layerGuess: String?): EquipTarget? {
        return when {
            partGuess != null && layerGuess != null -> body.findEquipTarget(item, partGuess, layerGuess)
            partGuess == null && layerGuess == null -> body.getDefaultTarget(item)
            partGuess != null -> body.findEquipTarget(item, partGuess)
            else -> body.findEquipTargetByLayer(item, layerGuess!!)
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

    private fun suggestsEquipPoint(source: Player, item: Thing, partGuess: String?) {
        val message = "Could not find equip point for $partGuess. Where would you like to equip ${item.name}?\n\t${item.equipTargets.joinToString("\n\t")}"
        val response = ResponseRequest(
            message,
            item.equipTargets.associate { it.toString() to "equip ${item.name} to ${it.layer} on ${it.parts.first()}" })
        CommandParsers.setResponseRequest(source, response)
    }

    private fun confirmEquip(source: Player, newEquip: Thing, equippedItems: List<Thing>, partGuess: String?) {
        val toPart = if (partGuess.isNullOrBlank()) "" else " to $partGuess"
        source.respond({}) {
            message("Replace ${equippedItems.joinToString(", ") { it.name }} with ${newEquip.name}?")
            yesNoOptions("equip ${newEquip.name}$toPart f", "")
        }
    }
}
