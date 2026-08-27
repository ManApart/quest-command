package inventory

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.delims
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import traveling.location.location.Location

class InventoryCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Bag", "b", "backpack")
    }

    override fun getDescription(): String {
        return "View and manage your inventory."
    }

    override fun getManual(): String {
        return """
	Bag - list items in your inventory.
	Bag <thing> - list items in the thing's inventory, if possible.
    Bag fit <item> - Will this item fit in your inventory?
    Bag fit <item> in <taker> - Will this item fit in taker's inventory?"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() || args.first() == "fit" -> (source.thing.currentLocation().getActivators(perceivedBy = source.thing) + source.thing.currentLocation()
                .getCreatures(perceivedBy = source.thing)).map { it.name }

            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val location = source.thing.currentLocation()
        val allInventories = location.findThingsByTag("Container")
        val argString = args.joinToString(" ")
        val thing = location.getThingsIncludingInventories(argString).firstOrNull()
        if (args.firstOrNull() == "fit") {
            executeFit(source, args, location)
            return
        }

        when {
            args.isEmpty() && allInventories.size == 1 -> EventManager.postEvent(ViewInventoryEvent(source, allInventories.first()))
            args.isEmpty() && keyword == "bag" -> clarifyThing(source, allInventories)
            args.isEmpty() -> EventManager.postEvent(ViewInventoryEvent(source, source.thing))
            thing != null -> EventManager.postEvent(ViewInventoryEvent(source, thing))
            else -> source.displayToMe("Could not find $argString")
        }
    }

    private fun clarifyThing(source: Player, things: List<Thing>) {
        source.respond({ EventManager.postEvent(ViewInventoryEvent(source, source.thing)) }) {
            message("View whose inventory?")
            optionsNamed(things)
            command { "bag $it" }
        }
    }

    private suspend fun executeFit(source: Player, args: List<String>, location: Location) {
        val arguments = Args(args, delims("in"))
        val takerGuess = arguments.getStringIfDelimExists("in")
        val taker = takerGuess?.let { location.getThings(it) } ?: listOf(source.thing)
        val itemString = arguments.getBaseString().replace("fit", "").trim()
        val thing = location.getThingsIncludingInventories(itemString).firstOrNull()
        when {
            thing == null && itemString.isBlank() -> source.respond("Couldn't find items.") {
                message("Can what fit?")
                optionsNamed(location.getThings())
                command { "bag fit $it" }
            }

            thing == null -> source.displayToMe("Couldn't find $itemString.")
            taker.size != 1 -> source.respond("Couldn't find Taker") {
                message("Check fit on what taker?")
                optionsNamed(taker)
                command { "bag fit ${thing.name} in $it" }
            }

            else -> EventManager.postEvent(ViewInventoryFitEvent(source, taker.first(), thing))
        }
    }

}
