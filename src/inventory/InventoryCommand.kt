package inventory

import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import interact.ScopeManager
import core.gameState.targetsToString
import system.EventManager

class InventoryCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Inventory", "b", "bag", "backpack")
    }

    override fun getDescription(): String {
        return "Inventory:\n\tView and manage your inventory"
    }

    override fun getManual(): String {
        return "\n\tInventory - list items in your inventory" +
                "\n\tInventory equip <item> - equip an item X"
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            listInventory()
        } else {
            println("Unknown command: ${args.joinToString(" ")}")
        }
    }

    private fun listInventory() {
        val itemList = targetsToString(GameState.player.inventory.items)
        println("You have $itemList in your inventory")
    }

}