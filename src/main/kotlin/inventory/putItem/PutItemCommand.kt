package inventory.putItem

import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
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
	Put <item> in <thing> - Place an item from your inventory into another container."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "in")))
        val arguments = Args(args, delimiters)
        when {
            arguments.isEmpty() && keyword == "put" -> clarifyItemToPlace(source)
            arguments.hasBase() && (arguments.hasGroup("in") || arguments.hasGroup("to")) -> placeItemInContainer(source, arguments)
            else -> source.displayToMe("Place what where? Try 'place <item> in <thing>'.")
        }
    }

    private fun clarifyItemToPlace(source: Thing) {
        val things = source.inventory.getItems().map { it.name }
        val message = "Give what item?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "place $it in" }))
    }

    private fun placeItemInContainer(source: Thing, args: Args) {
        val item = source.inventory.getItem(args.getBaseString())
        if (item != null) {
            val thingString = args.getFirstString("in", "to")
            val destinations = source.currentLocation().getThings(thingString).filterUniqueByName()
            when {
                thingString.isNotBlank() && destinations.isEmpty() -> source.displayToMe("Couldn't find $thingString")
                destinations.size == 1 -> EventManager.postEvent(TransferItemEvent(source, item, source, destinations.first(), true))
                else -> giveToWhat(destinations, args.getBaseString())
            }
        } else {
            source.displayToMe("Couldn't find ${args.getBaseString()}")
        }
    }

    private fun giveToWhat(creatures: List<Thing>, itemName: String) {
        val message = "Give $itemName to what?\n\t${creatures.joinToString(", ")}"
        val response = ResponseRequest(message, creatures.associate { it.name to "give $itemName to ${it.name}" })
         CommandParser.setResponseRequest(response)
    }


}