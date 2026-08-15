package inventory.equipItem

import core.Player
import core.body.Body2
import core.body.BodyPartStrings.LEFT_HAND
import core.body.BodyPartStrings.RIGHT_HAND
import core.body.EquipLayerStrings.GRIP
import core.body.EquipTarget
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

class HoldItemCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Hold", "Grab")
    }

    override fun getDescription(): String {
        return "Hold an item."
    }

    override fun getManual(): String {
        return """
	Hold <item> - Hold an item in an open hand.
	Hold <item> in <hand> - Hold an item in a specific hand.
    To hold an item it must be small enough and your hand must be free.
	"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> source.thing.currentLocation().getThingsIncludingInventories(source.thing).map { it.name }
            args.size == 1 -> listOf("in")
            args.last() == "in" -> listOf("right", "left")
            args.last() in listOf("right", "left") -> listOf("hand")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delims("in"), flags = listOf("f"))

        if (arguments.isEmpty()) {
            suggestEquippableItems(source)
        } else {
            val item = getItem(source.thing, arguments)
            val handGuess = arguments.getStringIfDelimExists("in")
            val body = source.body2
            val force = arguments.hasFlag("f")

            if (item == null) {
                source.displayToMe("Could not find ${arguments.getBaseString()}. (Did you mean 'hold item in <hand>?")
            } else {
                if (!body.canEquip(item)) {
                    source.displayToMe("You can't hold ${item.name}.")
                } else {
                    val target = findTarget(handGuess, body, item)
                    if (target == null) {
                        suggestHand(source, handGuess, item)
                    } else {
                        val equippedItems = body.getEquippedAt(target)
                        if (equippedItems.isNotEmpty() && !force) {
                            confirmEquip(source, item, equippedItems, handGuess)
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
        return source.currentLocation().getItemsIncludingPlayerInventory(itemName, source).firstOrNull()
    }

    private fun findTarget(handGuess: String?, body: Body2, item: Thing): EquipTarget? {
        return if (handGuess == null) {
            body.getDefaultTarget(item)
        } else {
            body.findEquipTarget(item, handGuess, GRIP)
        }
    }

    private suspend fun suggestEquippableItems(source: Player) {
        source.respondSuspend("There is nothing for you to hold.") {
            message("What do you want to hold?")
            optionsNamed(getEquipableItems(source.thing))
            command { "hold $it" }
        }
    }

    private fun getEquipableItems(source: Thing): List<Thing> {
        val body = source.body2
        val equippedItems = body.getEquipped()
        return source.inventory.getAllItems().filter { body.canEquip(it) && !equippedItems.contains(it) }
    }

    private suspend fun suggestHand(source: Player, handGuess: String?, item: Thing) {
        source.respondSuspend("There is no place you can hold ${item.name}.") {
            message("Could not find attach point $handGuess. Where would you like to hold ${item.name}?")
            options(RIGHT_HAND, LEFT_HAND)
            command { "hold ${item.name} in $it" }
        }
    }

    private fun confirmEquip(source: Player, newEquip: Thing, equippedItems: List<Thing>, partGuess: String?) {
        val toPart = if (partGuess.isNullOrBlank()) "" else " to $partGuess"
        source.respond({}) {
            message("Replace ${equippedItems.joinToString(", "){it.name}} with ${newEquip.name}?")
            yesNoOptions("hold ${newEquip.name}$toPart f", "")
        }
    }
}
