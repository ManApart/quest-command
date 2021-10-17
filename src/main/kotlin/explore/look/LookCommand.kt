package explore.look

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.thing.Thing

class LookCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Look", "ls")
    }

    override fun getDescription(): String {
        return "Observe your surroundings."
    }

    override fun getManual(): String {
        return """
	Look all - View the objects you can interact with.
	Look <thing> - Look at a specific thing."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "look" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(LookEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(LookEvent(source))
            source.currentLocation().getThingsIncludingPlayerInventory(source, argString).isNotEmpty() -> EventManager.postEvent(LookEvent(source, source.currentLocation().getThingsIncludingPlayerInventory(
                source,
                argString
            ).first()))
            else -> source.display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyThing(source: Thing) {
        val things  = (listOf("all") + source.currentLocation().getThings().map { it.name })
        val message = "Look at what?\n\t${things.joinToString(", ")}"
        val response = ResponseRequest(message, things.associateWith { "look $it" })
        CommandParser.setResponseRequest(response)
    }

}