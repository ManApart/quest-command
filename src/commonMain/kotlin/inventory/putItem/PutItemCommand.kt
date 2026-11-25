package inventory.putItem

import core.Player
import core.commands.ArgDelimiter
import core.commands.Args
import core.commands.pickThing
import core.commands.respond
import core.commands.respondSuspend
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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.inventory.getAllItems().map { it.name } + source.location.getLocation().getItems(perceivedBy = source.thing).map { it.name }
            args.size == 1 -> listOf("in")
            args.last() == "in" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("to", "in")))
        val arguments = Args(args, delimiters)
        when {
            arguments.isEmpty() && keyword == "put" -> clarifyItemToPlace(source)
            arguments.hasBase() && (arguments.hasGroup("in") || arguments.hasGroup("to")) -> placeItemInContainer(source, arguments)
            else -> source.displayToMe("Place what where? Try 'place <item> in <thing>'.")
        }
    }

    private suspend fun clarifyItemToPlace(source: Player) {
        source.respondSuspend("You don't have any items to give."){
            message("Give what item?")
            optionsNamed(source.inventory.getItems())
            command { "place $it in" }
        }
    }

    private suspend fun placeItemInContainer(source: Player, args: Args) {
        val item = source.inventory.getItem(args.getBaseString())
        if (item != null) {
            val thingString = args.getFirstString("in", "to")
            val destination = source.thing.currentLocation().getThingsIncludingInventories(thingString).perceivedBy(source.thing).pickThing(source, "Give $thingString to what?", {t -> "give $thingString to $t"}) ?: return

            EventManager.postEvent(TransferItemEvent(source.thing, item, source.thing, destination, true))
        } else {
            source.displayToMe("Couldn't find ${args.getBaseString()}")
        }
    }
}
