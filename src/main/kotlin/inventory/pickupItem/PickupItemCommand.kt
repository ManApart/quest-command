package inventory.pickupItem

import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import core.utility.filterUniqueByName
import interact.scope.ScopeManager
import inventory.dropItem.TransferItemEvent
import system.EventManager

class PickupItemCommand : core.commands.Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Pickup", "p", "get", "add", "take", "grab")
    }

    override fun getDescription(): String {
        return "Pickup:\n\tAdd an item to your inventory."
    }

    override fun getManual(): String {
        return "\n\tPickup <item> - pickup an item." +
                "\n\tPickup <item> from <target> - take item from target's inventory, if possible."
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from"))
        when {
            args.isEmpty() -> pickupWhat(ScopeManager.getScope().getItems().filterUniqueByName())
            arguments.hasGroup("from") -> pickupItemFromContainer(arguments)
            else -> pickupItemFromScope(arguments)
        }
    }

    private fun pickupItemFromScope(args: Args) {
        val items = ScopeManager.getScope().getItems(args.getBaseString()).filterUniqueByName()
        when {
            items.isEmpty() -> display("Couldn't find ${args.getBaseString()}")
            items.size == 1 -> EventManager.postEvent(TransferItemEvent(GameState.player, items.first(), destination = GameState.player))
            else -> pickupWhat(items)
        }
    }

    private fun pickupWhat(items: List<Target>) {
        if (items.isEmpty()) {
            display("Nothing to pickup!")
        } else {
            val message = "Pickup which item?\n\t${items.joinToString(", ")}"
            val response = ResponseRequest(message, items.map { it.name to "take ${it.name}" }.toMap())
             CommandParser.setResponseRequest(response)
        }
    }

    private fun pickupItemFromContainer(args: Args) {
        val from = ScopeManager.getScope().getTargets(args.getString("from")).filterUniqueByName()
        when {
            from.isEmpty() -> display("Couldn't find ${args.getString("from")}")
            from.size == 1 -> takeItemFromContainer(from.first(), args.getBaseString())
            else -> takeFromWhat(from, args.getBaseString())
        }
    }

    private fun takeFromWhat(creatures: List<Target>, itemName: String) {
        val message = "Take $itemName from what?\n\t${creatures.joinToString(", ")}"
        val response = ResponseRequest(message, creatures.map { it.name to "take $itemName from ${it.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }

    private fun takeItemFromContainer(from: Target, itemName: String) {
        val item = from.inventory.getItem(itemName)
        if (item != null) {
            EventManager.postEvent(TransferItemEvent(GameState.player, item, from, GameState.player))
        } else {
            display("Couldn't find $itemName")
        }
    }

}