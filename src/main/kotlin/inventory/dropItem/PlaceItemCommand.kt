package inventory.dropItem

import core.gameState.GameState
import core.history.display
import system.EventManager

class PlaceItemCommand : core.commands.Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Place", "Drop", "Give")
    }

    override fun getDescription(): String {
        return "Place:\n\tPlace an item from your inventory in another inventory or on the ground."
    }

    override fun getManual(): String {
        return "\n\tDrop <item> - Drop an item an item from your inventory." +
                "\n\tPlace <item> in <target> - Drop an item an item from your inventory. X"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.size == 1) {
            dropItem(args.joinToString(" "))
        } else {
            display("Drop what?")
        }
    }

    private fun dropItem(itemArgs: String) {
        val item = GameState.player.creature.inventory.getItem(itemArgs)
        if (item != null) {
            EventManager.postEvent(PlaceItemEvent(GameState.player.creature, item))
        } else {
            display("Couldn't find $itemArgs")
        }
    }
}