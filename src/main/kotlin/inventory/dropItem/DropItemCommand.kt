package inventory.dropItem

import core.GameState
import core.commands.*
import core.events.EventManager
import core.history.display
import traveling.position.Vector

class DropItemCommand : core.commands.Command() {

    override fun getAliases(): List<String> {
        return listOf("Drop", "Place")
    }

    override fun getDescription(): String {
        return "Drop an item from your inventory onto the ground."
    }

    override fun getManual(): String {
        return """
	Drop <item> - Drop an item an item from your inventory.
	Place <item> at <vector> - Place the item at a specific place."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("at"))
        val vector = parseVector(args, GameState.player.position)
        when {
            arguments.isEmpty() -> clarifyItemToDrop()
            arguments.hasBase() -> dropItem(arguments, vector)
            else -> display("Drop what? Try 'drop <item>'.")
        }
    }

    private fun clarifyItemToDrop() {
        val targets = GameState.player.inventory.getItems().map { it.name }
        val message = "Drop what item?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "drop $it" }))
    }

    private fun dropItem(args: Args, position: Vector) {
        val item = GameState.player.inventory.getItem(args.getBaseString())
        if (item != null) {
            EventManager.postEvent(PlaceItemEvent(GameState.player, item, position))
        } else {
            display("Couldn't find ${args.getBaseString()}")
        }
    }




}