package explore.examine

import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe

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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "examine" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(ExamineEvent(source.thing))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ExamineEvent(source.thing))
            source.thing.currentLocation().getThingsIncludingPlayerInventory(source.thing, argString).isNotEmpty() -> EventManager.postEvent(ExamineEvent(source.thing, source.thing.currentLocation().getThingsIncludingPlayerInventory(
                source.thing,
                argString
            ).first()))
            else -> source.displayToMe("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyThing(source: Player) {
        source.respond {
            message("Examine what?")
            options(listOf("all") + source.thing.currentLocation().getThings().map { it.name })
            command { "examine $it" }
        }
    }

}