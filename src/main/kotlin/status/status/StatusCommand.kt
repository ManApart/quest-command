package status.status

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.GameState
import core.history.display
import core.events.EventManager
import core.utility.filterUniqueByName

class StatusCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Status", "Info", "stats", "stat")
    }

    override fun getDescription(): String {
        return "Get information about your or something else's status"
    }

    override fun getManual(): String {
        return """
	Status - Get your current status
	Status <target> - Get the status of a target."""
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        when {
            args.isEmpty() && keyword == "status" -> clarifyStatus()
            args.isEmpty() -> EventManager.postEvent(StatusEvent(GameState.player))
            GameState.currentLocation().getCreatures(argsString).filterUniqueByName().isNotEmpty()-> EventManager.postEvent(StatusEvent(GameState.currentLocation().getCreatures(argsString).filterUniqueByName().first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyStatus() {
        val targets = GameState.currentLocation().getCreatures().map { it.name }
        val message = "Status of what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.associateWith { "status $it" }))
    }


}