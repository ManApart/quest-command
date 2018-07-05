package commands

import events.PickupItemEvent
import events.DropItemEvent
import gameState.GameState
import gameState.Item
import processing.EventManager
import processing.ItemManager
import processing.TargetManager

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
            listInventory()
        } else if (args.size > 1){
            val itemArgs = args.subList(1, args.size)
            if (ItemManager.itemExists(itemArgs)){
                val item = ItemManager.getItem(itemArgs)

                if (args[0] == "add"){
                    EventManager.postEvent(PickupItemEvent(GameState.player, item))
                } else if (args[0] == "drop"){
                    EventManager.postEvent(DropItemEvent(GameState.player, item))
                }
            }

        }
    }

    private fun listInventory() {
        val itemList = TargetManager.targetsToString(GameState.player.inventory.items)
        println("You have $itemList in your inventory")
    }
}