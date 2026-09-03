package inventory.pickupItem

import core.Player
import core.commands.Args
import core.commands.args
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
	Take <item> - Take an item.
	Take <item> from <thing> - Take item from thing's inventory, if possible.
	Take all from <thing> - Take everything you can from thing's inventory, if possible.
    Take <item> into <bag> - Place an item into a specific pouch/bag in your inventory
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
            args.last() == "into" -> source.inventory.getAllItems().filter { it.properties.isOpenContainer() }.map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = args(args, "from", "into")
        when {
            args.isEmpty() -> pickupWhat(source, source.thing.currentLocation().getItems().filterUniqueByName())
            arguments.hasGroup("from") -> pickupItemFromContainer(source, arguments)
            else -> pickupItemFromScope(source, arguments)
        }
    }

    private suspend fun pickupItemFromScope(source: Player, args: Args) {
        val items = source.thing.currentLocation().getItems(args.getBaseString()).filterUniqueByName()
        val into = if (args.hasGroup("into")) args.getString("into").let { source.inventory.getItem(it) } ?: source.thing else source.thing
        when {
            items.isEmpty() -> source.displayToMe("Couldn't find ${args.getBaseString()}")
            items.size == 1 -> EventManager.postEvent(TakeItemEvent(source.thing, items.first(), into))
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

    private suspend fun pickupItemFromContainer(source: Player, args: Args) {
        val from = source.thing.currentLocation().getThings(args.getString("from")).filterUniqueByName()
        val into = if (args.hasGroup("into")) args.getString("into").let { source.inventory.getItem(it) } ?: source.thing else source.thing
        when {
            from.isEmpty() -> source.displayToMe("Couldn't find ${args.getString("from")}.")
            from.size == 1 -> takeItemFromContainer(source.thing, from.first(), args.getBaseString(), into)
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

    private fun takeItemFromContainer(source: Thing, from: Thing, itemName: String, into: Thing) {
        if (itemName.lowercase() == "all") {
            takeAllFromContainer(source, from, into)
        } else {
            takeSingleItemFromContainer(source, from, itemName, into)
        }
    }

    private fun takeAllFromContainer(source: Thing, from: Thing, into: Thing) {
        from.inventory.getItems().forEach { item ->
            EventManager.postEvent(TransferItemEvent(source, item, from, into))
        }
    }

    private fun takeSingleItemFromContainer(source: Thing, from: Thing, itemName: String, into: Thing) {
        val item = from.inventory.getItem(itemName)
        if (item != null) {
            EventManager.postEvent(TransferItemEvent(source, item, from, into))
        } else {
            source.displayToMe("Couldn't find $itemName.")
        }
    }

}
