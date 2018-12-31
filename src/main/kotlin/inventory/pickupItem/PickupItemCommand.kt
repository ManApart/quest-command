package inventory.pickupItem

import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item
import core.history.display
import core.utility.filterUniqueByName
import interact.scope.ScopeManager
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
        if (args.isNotEmpty()) {
            pickupItem(args)
        } else {
            display("Pickup what?")
        }
    }

    private fun pickupItem(arguments: List<String>) {
        val args = Args(arguments, delimiters = listOf("from"))
        when {
            args.argStrings.size == 1 -> pickupItemFromScope(args)
            args.argStrings.size == 2 -> pickupItemFromContainer(args)
            else -> display("Take what from what? Try 'pickup <item>' or 'take <item> from <target>'.")
        }
    }

    private fun pickupItemFromScope(args: Args) {
        val items = ScopeManager.getScope().getItems(args.argStrings[0]).filterUniqueByName()
        when{
            items.isEmpty() -> display("Couldn't find ${args.argStrings[0]}")
            items.size == 1 -> EventManager.postEvent(PickupItemEvent(GameState.player.creature, items.first()))
            else -> pickupWhat(items)
        }
    }

    private fun pickupWhat(items: List<Item>) {
        display("Pickup which item?\n\t${items.joinToString(", ")}")
        val response = ResponseRequest(items.map { it.name to "take ${it.name}" }.toMap())
        CommandParser.setNextResponse(response)
    }

    private fun pickupItemFromContainer(args: Args) {
        val from = ScopeManager.getScope().getCreatures(args.argStrings[1]).filterUniqueByName()
        when{
            from.isEmpty() -> display("Couldn't find ${args.argStrings[1]}")
            from.size == 1 -> takeItemFromContainer(from.first(), args.argStrings[0])
            else -> takeFromWhat(from, args.argStrings[0])
        }
    }

    private fun takeFromWhat(creatures: List<Creature>, itemName: String) {
        display("Take from what?\n\t${creatures.joinToString(", ")}")
        val response = ResponseRequest(creatures.map { it.name to "take $itemName from ${it.name}" }.toMap())
        CommandParser.setNextResponse(response)
    }

    private fun takeItemFromContainer(from: Creature, itemName: String){
        val item = from.inventory.getItem(itemName)
        if (item != null) {
            EventManager.postEvent(PickupItemEvent(GameState.player.creature, item, from))
        } else {
            display("Couldn't find $itemName")
        }
    }

}