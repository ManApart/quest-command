package explore.examine

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.history.displayYou
import core.target.Target

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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "examine" && args.isEmpty() -> clarifyTarget(source)
            args.isEmpty() -> EventManager.postEvent(ExamineEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ExamineEvent(source))
            source.currentLocation().getTargetsIncludingPlayerInventory(source, argString).isNotEmpty() -> EventManager.postEvent(ExamineEvent(source, source.currentLocation().getTargetsIncludingPlayerInventory(
                source,
                argString
            ).first()))
            else -> source.displayYou("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyTarget(source: Target) {
        val targets  = (listOf("all") + source.currentLocation().getTargets().map { it.name })
        val message = "Examine what?\n\t${targets.joinToString(", ")}"
        val response = ResponseRequest(message, targets.associateWith { "examine $it" })
        CommandParser.setResponseRequest(response)
    }

}