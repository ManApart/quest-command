package inventory

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.commands.ResponseRequest
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val location = source.thing.currentLocation()
        val allInventories = location.findThingsByTag("Container")
        val argString = args.joinToString(" ")
        val thing = location.getThings(argString).firstOrNull()

        when {
            args.isEmpty() && allInventories.size == 1 -> EventManager.postEvent(ListInventoryEvent(allInventories.first()))
            args.isEmpty() && keyword == "bag" -> clarifyThing(source, allInventories)
            args.isEmpty() -> EventManager.postEvent(ListInventoryEvent(source.thing))
            thing != null -> EventManager.postEvent(ListInventoryEvent(thing))
            else -> source.displayToMe("Could not find $argString")
        }
    }

    private fun clarifyThing(source: Player, things: List<Thing>) {
        val names = things.map { it.name }
        val message = "View whose inventory?\n\t${names.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, names.associateWith { "bag $it" }))
        source.respond {
            message("View whose inventory?")
            options(things)
            command { "bag $it" }
        }
    }


}