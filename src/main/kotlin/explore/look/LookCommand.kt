package explore.look

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display

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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "look" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(LookEvent(source.thing))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(LookEvent(source.thing))
            source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, argString).isNotEmpty() -> EventManager.postEvent(LookEvent(source.thing, source.thing.currentLocation().getThingsIncludingPlayerInventory(
                source.thing,
                argString
            ).first()))
            else -> source.display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyThing(source: Player) {
        val things  = (listOf("all") + source.thing.currentLocation().getThings().map { it.name })
        val message = "Look at what?\n\t${things.joinToString(", ")}"
        val response = ResponseRequest(message, things.associateWith { "look $it" })
        CommandParsers.setResponseRequest(source, response)
    }

}