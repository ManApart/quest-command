package inventory

import core.commands.Command
import core.gameState.GameState
import core.gameState.targetsToString

class InventoryCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Bag", "b", "backpack")
    }

    override fun getDescription(): String {
        return "Inventory:\n\tView and manage your inventory"
    }

    override fun getManual(): String {
        return "\n\tInventory - list items in your inventory" +
                "\n\tInventory equip <item> - equip an item X"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
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