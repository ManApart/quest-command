package explore.look

import core.GameState
import core.commands.Command
import core.commands.CommandParser
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
	Look <target> - Look at a specific target."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "look" && args.isEmpty() -> clarifyTarget()
            args.isEmpty() -> EventManager.postEvent(LookEvent())
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(LookEvent())
            GameState.currentLocation().getTargetsIncludingPlayerInventory(argString).isNotEmpty() -> EventManager.postEvent(LookEvent(GameState.player, GameState.currentLocation().getTargetsIncludingPlayerInventory(argString).first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyTarget() {
        val targets  = (listOf("all") + GameState.currentLocation().getTargets().map { it.name })
        val message = "Look at what?\n\t${targets.joinToString(", ")}"
        val response = ResponseRequest(message, targets.associateWith { "look $it" })
        CommandParser.setResponseRequest(response)
    }

}