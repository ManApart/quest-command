package inventory

import core.gameState.GameState
import system.EventManager

class DropItemCommand : core.commands.Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Drop")
    }

    override fun getDescription(): String {
        return "Drop:\n\tDrop an item from your inventory"
    }

    override fun getManual(): String {
        return "\n\tDrop <item> - Drop an item an item from your inventory"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.size == 1) {
            dropItem(args)
        } else {
            println("Drop what?")
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