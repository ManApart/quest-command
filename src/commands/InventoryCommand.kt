package commands

import gameState.GameState

class InventoryCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Inventory", "b", "bag", "backpack")
    }

    override fun getDescription(): String {
        return "Inventory:\n\tView and manage your inventory"
    }

    override fun getManual(): String {
        return "\n\tInventory - list items in your inventory" +
                "\n\tInventory drop <item> - drop an item X" +
                "\n\tInventory equip <item> - equip an item X"
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()){
            println("You have ${GameState.player.items.joinToString(", ")} in your inventory")
        }
    }
}