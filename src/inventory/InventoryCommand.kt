package inventory

import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import use.ScopeManager
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
                "\n\tInventory pickup <item> - pickup an item" +
                "\n\tInventory drop <item> - drop an item X" +
                "\n\tInventory equip <item> - equip an item X"
    }

//    override fun execute(args: List<String>) {
//        if (args.isEmpty()) {
//            listInventory()
//        } else if (args.size > 1){
//            val itemArgs = args.subList(1, args.size)
//            if (ItemManager.itemExists(itemArgs)){
//                val item = ItemManager.getItem(itemArgs)
//
//                if (args[0] == "add" || args[0] == "pickup"){
//                    EventManager.postEvent(PickupItemEvent(GameState.player, item))
//                } else if (args[0] == "drop"){
//                    EventManager.postEvent(DropItemEvent(GameState.player, item))
//                } else {
//                    println("Unknown command: ${args.joinToString(" ")}")
//                }
//            } else {
//                println("Unable to find ${args.joinToString(" ")}")
//            }
//
//        }
//    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            listInventory()
        } else if (args.size > 1) {
            val itemArgs = args.subList(1, args.size)
            if (args[0] == "add" || args[0] == "pickup") {
                pickupItem(itemArgs)
            } else if (args[0] == "drop") {
                dropItem(itemArgs)
            } else {
                println("Unknown command: ${args.joinToString(" ")}")
            }

        }
    }

    private fun listInventory() {
        val itemList = targetsToString(GameState.player.inventory.items)
        println("You have $itemList in your inventory")
    }

    private fun pickupItem(itemArgs: List<String>) {
        if (ScopeManager.targetExistsOutsideInventory(itemArgs) && ScopeManager.getTargetExcludingInventory(itemArgs) is Item) {
            val item = ScopeManager.getTargetExcludingInventory(itemArgs) as Item
            EventManager.postEvent(PickupItemEvent(GameState.player, item))
        } else {
            println("Couldn't find ${itemArgs.joinToString(" ")}")
        }
    }

    private fun dropItem(itemArgs: List<String>) {
        if (GameState.player.inventory.itemExists(itemArgs)) {
            val item = GameState.player.inventory.getItem(itemArgs)
            EventManager.postEvent(DropItemEvent(GameState.player, item))
        } else {
            println("Couldn't find ${itemArgs.joinToString(" ")}")
        }
    }
}