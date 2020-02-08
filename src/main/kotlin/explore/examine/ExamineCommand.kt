package explore.examine

import core.GameState
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import traveling.scope.ScopeManager

class ExamineCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Examine", "Exa", "cat")
    }

    override fun getDescription(): String {
        return "Examine:\n\tExamine your surroundings in detail."
    }

    override fun getManual(): String {
        return "\n\tExamine all - Look more closely at your surroundings. Gives more detailed information than look, based on how perceptive you are." +
                "\n\tExamine <target> - Look closely at a specific target."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "examine" && args.isEmpty() -> clarifyTarget()
            args.isEmpty() -> EventManager.postEvent(ExamineEvent())
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ExamineEvent())
            ScopeManager.getScope().getTargetsIncludingPlayerInventory(argString).isNotEmpty() -> EventManager.postEvent(ExamineEvent(GameState.player, ScopeManager.getScope().getTargetsIncludingPlayerInventory(argString).first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyTarget() {
        val targets  = (listOf("all") + ScopeManager.getScope().getTargets().map { it.name })
        val message = "Examine what?\n\t${targets.joinToString(", ")}"
        val response = ResponseRequest(message, targets.map { it to "examine $it" }.toMap())
        CommandParser.setResponseRequest(response)
    }

}