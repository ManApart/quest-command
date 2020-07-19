package inventory.dropItem

import core.GameState
import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.filterUniqueByName

class DropItemCommand : core.commands.Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Drop")
    }

    override fun getDescription(): String {
        return "Drop:\n\tDrop an item from your inventory onto the ground."
    }

    override fun getManual(): String {
        return "\n\tDrop <item> - Drop an item an item from your inventory."
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args)
        when {
            arguments.isEmpty() -> clarifyItemToDrop()
            arguments.hasBase() -> dropItem(arguments)
            else -> display("Drop what? Try 'drop <item>'.")
        }
    }

    private fun dropItem(args: Args) {
        val item = GameState.player.inventory.getItem(args.getBaseString())
        if (item != null) {
            EventManager.postEvent(TransferItemEvent(GameState.player, item, GameState.player))
        } else {
            display("Couldn't find ${args.getBaseString()}")
        }
    }

    private fun clarifyItemToDrop() {
        val targets = GameState.player.inventory.getItems().map { it.name }
        val message = "Drop what item?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "drop $it" }.toMap()))
    }


}