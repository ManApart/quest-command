package explore.examine

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

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
	Examine <thing> - Look closely at a specific thing."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "examine" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(ExamineEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ExamineEvent(source))
            source.currentLocation().getThingsIncludingPlayerInventory(source, argString).isNotEmpty() -> EventManager.postEvent(ExamineEvent(source, source.currentLocation().getThingsIncludingPlayerInventory(
                source,
                argString
            ).first()))
            else -> source.displayToMe("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyThing(source: Thing) {
        val things  = (listOf("all") + source.currentLocation().getThings().map { it.name })
        val message = "Examine what?\n\t${things.joinToString(", ")}"
        val response = ResponseRequest(message, things.associateWith { "examine $it" })
        CommandParser.setResponseRequest(response)
    }

}