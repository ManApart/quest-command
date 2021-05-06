package inventory.pickupItem

import core.GameState
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.filterUniqueByName
import inventory.putItem.TransferItemEvent

class TakeItemCommand : core.commands.Command() {
    override fun getAliases(): List<String> {
        return listOf("Take", "pickup", "p", "get", "add", "grab")
    }

    override fun getDescription(): String {
        return "Add an item to your inventory."
    }

    override fun getManual(): String {
        return """
	Take <item> - take an item.
	Take <item> from <target> - take item from target's inventory, if possible."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from"))
        when {
            args.isEmpty() -> pickupWhat(GameState.currentLocation().getItems().filterUniqueByName())
            arguments.hasGroup("from") -> pickupItemFromContainer(arguments)
            else -> pickupItemFromScope(arguments)
        }
    }

    private fun pickupItemFromScope(args: Args) {
        val items = GameState.currentLocation().getItems(args.getBaseString()).filterUniqueByName()
        when {
            items.isEmpty() -> display("Couldn't find ${args.getBaseString()}")
            items.size == 1 -> EventManager.postEvent(TakeItemEvent(GameState.player, items.first()))
            else -> pickupWhat(items)
        }
    }

    private fun pickupWhat(items: List<Target>) {
        if (items.isEmpty()) {
            display("Nothing to pickup!")
        } else {
            val message = "Take which item?\n\t${items.joinToString(", ")}"
            val response = ResponseRequest(message, items.associate { it.name to "take ${it.name}" })
            CommandParser.setResponseRequest(response)
        }
    }

    private fun pickupItemFromContainer(args: Args) {
        val from = GameState.currentLocation().getTargets(args.getString("from")).filterUniqueByName()
        when {
            from.isEmpty() -> display("Couldn't find ${args.getString("from")}.")
            from.size == 1 -> takeItemFromContainer(from.first(), args.getBaseString())
            else -> takeFromWhat(from, args.getBaseString())
        }
    }

    private fun takeFromWhat(creatures: List<Target>, itemName: String) {
        val message = "Take $itemName from what?\n\t${creatures.joinToString(", ")}"
        val response = ResponseRequest(message, creatures.associate { it.name to "take $itemName from ${it.name}." })
        CommandParser.setResponseRequest(response)
    }

    private fun takeItemFromContainer(from: Target, itemName: String) {
        val item = from.inventory.getItem(itemName)
        if (item != null) {
            EventManager.postEvent(TransferItemEvent(GameState.player, item, from, GameState.player))
        } else {
            display("Couldn't find $itemName.")
        }
    }

}