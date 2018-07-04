package commands

import gameState.GameState
import gameState.Item

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
        if (args.isEmpty()) {
            val itemCounts = HashMap<Item, Int>()
            GameState.player.items.forEach {
                itemCounts[it] = itemCounts[it]?.plus(1) ?: 1
            }

            val itemList = itemCounts.entries.joinToString(", ") {
                if (it.value == 1){
                    it.key.name
                } else {
                    "${it.value}x ${it.key}"
                }
            }

            println("You have $itemList in your inventory")
        }
    }
}