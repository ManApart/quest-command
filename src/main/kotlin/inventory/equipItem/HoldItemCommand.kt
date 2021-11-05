package inventory.equipItem

import core.Player
import core.body.Body
import core.body.Slot
import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.Command
import core.commands.respond
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("in")))
        val arguments = Args(args, delimiters, flags = listOf("f"))

        if (arguments.isEmpty()) {
            suggestEquippableItems(source)
        } else {
            val item = getItem(source.thing, arguments)
            val attachPointGuess = getAttachPoint(arguments)
            val body = source.body
            val force = arguments.hasFlag("f")

            if (item == null) {
                source.displayToMe("Could not find ${arguments.getBaseString()}. (Did you mean 'hold item in <hand>?")
            } else {
                if (!item.canEquipTo(body)) {
                    source.displayToMe("You can't hold ${item.name}.")
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

    private fun getItem(source: Thing, args: Args): Thing? {
        val itemName = args.getBaseString()
        return source.currentLocation().getItemsIncludingPlayerInventory(itemName, source).firstOrNull()
    }

    private fun getAttachPoint(args: Args): String? {
        return if (args.hasGroup("in")) args.getString("in") else null
    }

    private fun findSlot(attachPointGuess: String?, body: Body, item: Thing): Slot? {
        return if (attachPointGuess == null) {
           body.getEmptyEquipSlot(item) ?: body.getDefaultSlot(item)
        } else {
            item.findSlot(body, attachPointGuess)
        }
    }

    private fun suggestEquippableItems(source: Player) {
        source.respond {
            message("What do you want to hold?")
            options(getEquipableItems(source.thing))
            command { "hold $it" }
        }
    }

    private fun getEquipableItems(source: Thing): List<Thing> {
        val body = source.body
        val equippedItems = body.getEquippedItems()
        return source.inventory.getAllItems().filter { it.canEquipTo(body) && !equippedItems.contains(it) }
    }

    private fun suggestAttachPoints(source: Player, attachPointGuess: String?, item: Thing) {
        source.respond {
            message("Could not find attach point $attachPointGuess. Where would you like to hold ${item.name}?")
            val options = source.thing.body.getParts().filter { it.hasAttachPoint("Grip") }
            options(options)
            command { "hold ${item.name} in $it" }
        }

    }

    private fun confirmEquip(source: Player, newEquip: Thing, equippedItems: List<Thing>, attachPoint: String?) {
        val toPart = if (attachPoint.isNullOrBlank()) "" else " to $attachPoint"
        source.respond {
            message("Replace ${equippedItems.joinToString(", "){it.name}} with ${newEquip.name}?")
            yesNoOptions("hold ${newEquip.name}$toPart f", "")
            //TODO - no location to the north. Should swallow that
        }
    }
}