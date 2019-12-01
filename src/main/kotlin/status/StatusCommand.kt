package status

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.history.display
import core.utility.filterUniqueByName
import interact.scope.ScopeManager
import system.EventManager

class StatusCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Status", "Info", "stats", "stat")
    }

    override fun getDescription(): String {
        return "Status:\n\tGet information about your or something else's status"
    }

    override fun getManual(): String {
        return "\n\tStatus - Get your current status" +
                "\n\tStatus <target> - Get the status of a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        when {
            args.isEmpty() && keyword == "status" -> clarifyStatus()
            args.isEmpty() -> EventManager.postEvent(StatusEvent(GameState.player))
            ScopeManager.getScope().getCreatures(argsString).filterUniqueByName().isNotEmpty()-> EventManager.postEvent(StatusEvent(ScopeManager.getScope().getCreatures(argsString).filterUniqueByName().first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyStatus() {
        val targets = ScopeManager.getScope().getCreatures().map { it.name }
        val message = "Status of what?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.map { it to "status $it" }.toMap()))
    }


}