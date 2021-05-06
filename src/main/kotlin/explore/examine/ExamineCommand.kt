package explore.examine

import core.GameState
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display

class ExamineCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Examine", "Exa", "cat")
    }

    override fun getDescription(): String {
        return "Examine your surroundings in detail."
    }

    override fun getManual(): String {
        return """
	Examine all - Look more closely at your surroundings. Gives more detailed information than look, based on how perceptive you are.
	Examine <target> - Look closely at a specific target."""
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
            GameState.currentLocation().getTargetsIncludingPlayerInventory(argString).isNotEmpty() -> EventManager.postEvent(ExamineEvent(GameState.player, GameState.currentLocation().getTargetsIncludingPlayerInventory(argString).first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyTarget() {
        val targets  = (listOf("all") + GameState.currentLocation().getTargets().map { it.name })
        val message = "Examine what?\n\t${targets.joinToString(", ")}"
        val response = ResponseRequest(message, targets.associateWith { "examine $it" })
        CommandParser.setResponseRequest(response)
    }

}