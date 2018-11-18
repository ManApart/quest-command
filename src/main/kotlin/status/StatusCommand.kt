package status

import core.commands.Command
import core.gameState.GameState
import core.history.display
import interact.ScopeManager
import system.EventManager

class StatusCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Status")
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
        when {
            args.isEmpty() -> EventManager.postEvent(StatusEvent(GameState.player.creature))
            ScopeManager.activatorExists(args) -> EventManager.postEvent(StatusEvent(ScopeManager.getActivator(args).creature))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }


}