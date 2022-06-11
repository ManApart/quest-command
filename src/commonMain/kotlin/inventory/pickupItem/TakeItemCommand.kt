package inventory.pickupItem

import core.Player
import core.commands.Args
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import core.utility.filterUniqueByName
import inventory.putItem.TransferItemEvent

class TakeItemCommand : core.commands.Command() {
    override fun getAliases(): List<String> {
        return listOf("Take", "pickup", "p", "get", "add")
    }

    override fun getDescription(): String {
        return "Add an item to your inventory."
    }

    override fun getManual(): String {
        return """
	Take <item> - take an item.
	Take <item> from <thing> - take item from thing's inventory, if possible.
	Take all from <thing> - take everything you can from thing's inventory, if possible.
"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from"))
        when {
            args.isEmpty() -> pickupWhat(source, source.thing.currentLocation().getItems().filterUniqueByName())
            arguments.hasGroup("from") -> pickupItemFromContainer(source, arguments)
            else -> pickupItemFromScope(source, arguments)
        }
    }

    private fun pickupItemFromScope(source: Player, args: Args) {
        val items = source.thing.currentLocation().getItems(args.getBaseString()).filterUniqueByName()
        when {
            items.isEmpty() -> source.displayToMe("Couldn't find ${args.getBaseString()}")
            items.size == 1 -> EventManager.postEvent(TakeItemEvent(source.thing, items.first()))
            else -> pickupWhat(source, items)
        }
    }

    private fun pickupWhat(source: Player, items: List<Thing>) {
        if (items.isEmpty()) {
            source.displayToMe("Nothing to pickup!")
        } else {
            source.respond("There are no items for you to take.") {
                message("Take which item?")
                optionsNamed(items)
                command { "take $it" }
            }
        }
    }

    private fun pickupItemFromContainer(source: Player, args: Args) {
        val from = source.thing.currentLocation().getThings(args.getString("from")).filterUniqueByName()
        when {
            from.isEmpty() -> source.displayToMe("Couldn't find ${args.getString("from")}.")
            from.size == 1 -> takeItemFromContainer(source.thing, from.first(), args.getBaseString())
            else -> takeFromWhat(source, from, args.getBaseString())
        }
    }

    private fun takeFromWhat(source: Player, creatures: List<Thing>, itemName: String) {
        source.respond("Nothing to take from.") {
            message("Take $itemName from what?")
            optionsNamed(creatures)
            command { "take $itemName from $it." }
        }
    }

    private fun takeItemFromContainer(source: Thing, from: Thing, itemName: String) {
        if (itemName.lowercase() == "all") {
            takeAllFromContainer(source, from)
        } else {
            takeSingleItemFromContainer(source, from, itemName)
        }
    }

    private fun takeAllFromContainer(source: Thing, from: Thing) {
        from.inventory.getItems().forEach { item ->
            EventManager.postEvent(TransferItemEvent(source, item, from, source))
        }
    }

    private fun takeSingleItemFromContainer(source: Thing, from: Thing, itemName: String) {
        val item = from.inventory.getItem(itemName)
        if (item != null) {
            EventManager.postEvent(TransferItemEvent(source, item, from, source))
        } else {
            source.displayToMe("Couldn't find $itemName.")
        }
    }

}