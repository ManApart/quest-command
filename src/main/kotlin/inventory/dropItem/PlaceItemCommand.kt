package inventory.dropItem

import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.GameState
import core.target.Target
import core.history.display
import core.utility.filterUniqueByName
import traveling.scope.ScopeManager
import core.events.EventManager

class PlaceItemCommand : core.commands.Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Place", "Drop", "Give", "Put")
    }

    override fun getDescription(): String {
        return "Place:\n\tPlace an item from your inventory in another inventory or on the ground."
    }

    override fun getManual(): String {
        return "\n\tDrop <item> - Drop an item an item from your inventory." +
                "\n\tPlace <item> in <target> - Drop an item an item from your inventory. "
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "in")))
        val arguments = Args(args, delimiters)
        when {
            arguments.isEmpty() && keyword == "drop" -> clarifyItemToDrop()
            arguments.isEmpty() && keyword == "place" -> clarifyItemToPlace()
            arguments.hasBase() && arguments.hasGroup("in") -> placeItemInContainer(arguments)
            arguments.hasBase() -> dropItem(arguments)
            else -> display("Place what where? Try 'drop <item>' or 'place <item> in <target>'.")
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

    private fun placeItemInContainer(args: Args) {
        val item = GameState.player.inventory.getItem(args.getBaseString())
        if (item != null) {
            val targetString = args.getString("in")
            val destinations = ScopeManager.getScope().getTargets(targetString).filterUniqueByName()
            when {
                targetString.isNotBlank() && destinations.isEmpty() -> display("Couldn't find $targetString")
                destinations.size == 1 -> EventManager.postEvent(TransferItemEvent(GameState.player, item, GameState.player, destinations.first(), true))
                else -> giveToWhat(destinations, args.getBaseString())
            }
        } else {
            display("Couldn't find ${args.getBaseString()}")
        }
    }

    private fun clarifyItemToDrop() {
        val targets = GameState.player.inventory.getItems().map { it.name }
        val message = "Drop what item?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "drop $it" }.toMap()))
    }

    private fun clarifyItemToPlace() {
        val targets = GameState.player.inventory.getItems().map { it.name }
        val message = "Give what item?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.map { it to "place $it in" }.toMap()))
    }

    private fun giveToWhat(creatures: List<Target>, itemName: String) {
        val message = "Give $itemName to what?\n\t${creatures.joinToString(", ")}"
        val response = ResponseRequest(message, creatures.map { it.name to "give $itemName to ${it.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }


}