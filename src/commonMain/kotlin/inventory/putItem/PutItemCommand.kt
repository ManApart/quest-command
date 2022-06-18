package inventory.putItem

import core.Player
import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import core.thing.perceivedBy
import core.utility.filterUniqueByName

class PutItemCommand : core.commands.Command() {

    override fun getAliases(): List<String> {
        return listOf("Put", "Give", "place", "store")
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

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.inventory.getAllItems().map { it.name } + source.location.getLocation().getItems(perceivedBy = source.thing).map { it.name }
            args.size == 1 -> listOf("in")
            args.last() == "in" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "in")))
        val arguments = Args(args, delimiters)
        when {
            arguments.isEmpty() && keyword == "put" -> clarifyItemToPlace(source)
            arguments.hasBase() && (arguments.hasGroup("in") || arguments.hasGroup("to")) -> placeItemInContainer(source, arguments)
            else -> source.displayToMe("Place what where? Try 'place <item> in <thing>'.")
        }
    }

    private fun clarifyItemToPlace(source: Player) {
        source.respond("You don't have any items to give."){
            message("Give what item?")
            optionsNamed(source.inventory.getItems())
            command { "place $it in" }
        }
    }

    private fun placeItemInContainer(source: Player, args: Args) {
        val item = source.inventory.getItem(args.getBaseString())
        if (item != null) {
            val thingString = args.getFirstString("in", "to")
            val destinations = source.thing.currentLocation().getThingsIncludingInventories(thingString).perceivedBy(source.thing).filterUniqueByName()
            when {
                thingString.isNotBlank() && destinations.isEmpty() -> source.displayToMe("Couldn't find $thingString")
                destinations.size == 1 -> EventManager.postEvent(TransferItemEvent(source.thing, item, source.thing, destinations.first(), true))
                else -> giveToWhat(source, destinations, args.getBaseString())
            }
        } else {
            source.displayToMe("Couldn't find ${args.getBaseString()}")
        }
    }

    private fun giveToWhat(source: Player, creatures: List<Thing>, itemName: String) {
        source.respond("Couldn't find something to give $itemName to.") {
            message("Give $itemName to what?")
            optionsNamed(creatures)
            command { "give $itemName to $it" }
        }
    }


}