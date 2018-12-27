package inventory.pickupItem

import core.commands.Args
import core.gameState.GameState
import core.gameState.getCreature
import core.history.display
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
        val item = ScopeManager.getScope().getItem(args.argStrings[0])
        if (item != null) {
            EventManager.postEvent(PickupItemEvent(GameState.player.creature, item))
        } else {
            display("Couldn't find ${args.argStrings[0]}")
        }
    }

    private fun pickupItemFromContainer(args: Args) {
        val from = ScopeManager.getScope().getTarget(args.argStrings[1])?.getCreature()
        if (from != null) {
            val item = from.inventory.getItem(args.argStrings[0])
            if (item != null) {
                EventManager.postEvent(PickupItemEvent(GameState.player.creature, item, from))
            } else {
                display("Couldn't find ${args.argStrings[1]}")
            }
        } else {
            display("Couldn't find ${args.argStrings[1]}")
        }
    }
}