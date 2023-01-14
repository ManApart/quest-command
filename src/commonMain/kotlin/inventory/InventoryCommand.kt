package inventory

import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

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
	Bag <thing> - list items in the thing's inventory, if possible."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> (source.thing.currentLocation().getActivators(perceivedBy = source.thing) + source.thing.currentLocation().getCreatures(perceivedBy = source.thing)).map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val location = source.thing.currentLocation()
        val allInventories = location.findThingsByTag("Container")
        val argString = args.joinToString(" ")
        val thing = location.getThingsIncludingInventories(argString).firstOrNull()

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


}