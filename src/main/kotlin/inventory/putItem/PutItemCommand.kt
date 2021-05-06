package inventory.putItem

import core.GameState
import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.filterUniqueByName

class PutItemCommand : core.commands.Command() {

    override fun getAliases(): List<String> {
        return listOf("Put", "Give")
    }

    override fun getDescription(): String {
        return "Put an item from your inventory into another container."
    }

    override fun getManual(): String {
        return """
	Put <item> in <target> - Place an item from your inventory into another container."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "in")))
        val arguments = Args(args, delimiters)
        when {
            arguments.isEmpty() && keyword == "put" -> clarifyItemToPlace()
            arguments.hasBase() && (arguments.hasGroup("in") || arguments.hasGroup("to")) -> placeItemInContainer(arguments)
            else -> display("Place what where? Try 'place <item> in <target>'.")
        }
    }

    private fun clarifyItemToPlace() {
        val targets = GameState.player.inventory.getItems().map { it.name }
        val message = "Give what item?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "place $it in" }))
    }

    private fun placeItemInContainer(args: Args) {
        val item = GameState.player.inventory.getItem(args.getBaseString())
        if (item != null) {
            val targetString = args.getFirstString("in", "to")
            val destinations = GameState.currentLocation().getTargets(targetString).filterUniqueByName()
            when {
                targetString.isNotBlank() && destinations.isEmpty() -> display("Couldn't find $targetString")
                destinations.size == 1 -> EventManager.postEvent(TransferItemEvent(GameState.player, item, GameState.player, destinations.first(), true))
                else -> giveToWhat(destinations, args.getBaseString())
            }
        } else {
            display("Couldn't find ${args.getBaseString()}")
        }
    }

    private fun giveToWhat(creatures: List<Target>, itemName: String) {
        val message = "Give $itemName to what?\n\t${creatures.joinToString(", ")}"
        val response = ResponseRequest(message, creatures.associate { it.name to "give $itemName to ${it.name}" })
         CommandParser.setResponseRequest(response)
    }


}