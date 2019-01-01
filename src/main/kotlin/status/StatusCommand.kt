package status

import core.commands.Command
import core.gameState.GameState
import core.history.display
import core.utility.filterUniqueByName
import interact.scope.ScopeManager
import system.EventManager

class StatusCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Status", "Info")
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
            args.isEmpty() -> EventManager.postEvent(StatusEvent(GameState.player.creature))
            ScopeManager.getScope().getTargetsWithCreatures(argsString).filterUniqueByName().isNotEmpty()-> EventManager.postEvent(StatusEvent(ScopeManager.getScope().getTargetsWithCreatures(argsString).filterUniqueByName().first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }


}