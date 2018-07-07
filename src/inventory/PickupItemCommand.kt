package inventory

import core.gameState.GameState
import core.gameState.Item
import system.EventManager
import use.ScopeManager

class PickupItemCommand : core.commands.Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Pickup", "p", "get", "add")
    }

    override fun getDescription(): String {
        return "Pickup:\n\tAdd an item to your inventory"
    }

    override fun getManual(): String {
        return "\n\tPickup <item> - pickup an item"
    }

    override fun execute(args: List<String>) {
        if (args.size == 1) {
            pickupItem(args)
        } else {
            println("Pickup what?")
        }

    }

    private fun pickupItem(itemArgs: List<String>) {
        if (ScopeManager.targetExistsOutsideInventory(itemArgs) && ScopeManager.getTargetExcludingInventory(itemArgs) is Item) {
            val item = ScopeManager.getTargetExcludingInventory(itemArgs) as Item
            EventManager.postEvent(PickupItemEvent(GameState.player, item))
        } else {
            println("Couldn't find ${itemArgs.joinToString(" ")}")
        }
    }
}