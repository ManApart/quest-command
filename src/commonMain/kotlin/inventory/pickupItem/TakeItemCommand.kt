package inventory.pickupItem

import core.Player
import core.commands.Args
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import core.utility.filterUniqueByName
import inventory.putItem.TransferItemEvent
import kotlin.math.min

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
	Take *<count> <item> - take X amount (or all) items.
	Take <item> from <thing> - take item from thing's inventory, if possible.
	Take all from <thing> - take everything you can from thing's inventory, if possible.
"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        val things = source.location.getLocation().getThings(source.thing)
        return when {
            args.isEmpty() -> listOf("all") + things.map { it.name } + things.flatMap { it.inventory.getAllItems() }.map { it.name }
            args.size == 1 -> listOf("from")
            args.last() == "from" -> things.map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from"))
        val count = arguments.getNumber() ?: 1
        val takeAll = args.first() == "all"
        when {
            args.isEmpty() -> pickupWhat(source, source.thing.currentLocation().getItems().filterUniqueByName())
            arguments.hasGroup("from") -> pickupItemFromContainer(source, arguments, takeAll, count)
            else -> pickupItemFromScope(source, arguments, takeAll, count)
        }
    }

    private suspend fun pickupItemFromScope(source: Player, args: Args, takeAll: Boolean, count: Int) {
        val items = source.thing.currentLocation().getItems(args.getBaseString()).filterUniqueByName()
        when {
            items.isEmpty() -> source.displayToMe("Couldn't find ${args.getBaseString()}")
            items.size == 1 -> {
                val item = items.first()
                val itemCount = item.getItemCount(takeAll, count)
                EventManager.postEvent(TakeItemEvent(source.thing, item, itemCount))
            }

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

    private suspend fun pickupItemFromContainer(source: Player, args: Args, takeAll: Boolean, count: Int) {
        val from = source.thing.currentLocation().getThings(args.getString("from")).filterUniqueByName()
        when {
            from.isEmpty() -> source.displayToMe("Couldn't find ${args.getString("from")}.")
            from.size == 1 -> takeItemFromContainer(source.thing, from.first(), args.getBaseString(), takeAll, count)
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

    private suspend fun takeItemFromContainer(source: Thing, from: Thing, itemName: String, takeAllCount: Boolean, desiredCount: Int) {
        if (itemName.lowercase() == "all") {
            takeAllFromContainer(source, from)
        } else {
            takeItemsFromContainer(source, from, itemName, takeAllCount, desiredCount)
        }
    }

    private suspend fun takeAllFromContainer(source: Thing, from: Thing) {
        from.inventory.getItems().forEach { item ->
            EventManager.postEvent(TransferItemEvent(source, item, from, source))
        }
    }

    private suspend fun takeItemsFromContainer(source: Thing, from: Thing, itemName: String, takeAllCount: Boolean, desiredCount: Int) {
        val item = from.inventory.getItem(itemName)
        if (item != null) {
            val count = item.getItemCount(takeAllCount, desiredCount)
            EventManager.postEvent(TransferItemEvent(source, item, from, source, count))
        } else {
            source.displayToMe("Couldn't find $itemName.")
        }
    }

}

fun Thing.getItemCount(takeAll: Boolean, desiredCount: Int): Int {
    return if (takeAll) properties.getCount() else desiredCount
}