package use.interaction.nothing

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

class NothingCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Nothing", "Wait", "nn")
    }

    override fun getDescription(): String {
        return "Nothing - Do Nothing."
    }

    override fun getManual(): String {
        return """
    Like resting, but less useful.
    Nothing <duration> - Do nothing for a set amount of time."""
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("1", "5", "10")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        when {
            args.isEmpty() && keyword == "Nothing" -> clarifyHours(source)
            args.isEmpty() -> wait(source.thing, 1)
            args.size == 1 && arguments.getNumber() != null -> wait(source.thing, arguments.getNumber()!!)
            else -> source.displayToMe("Unknown params for rest: ${args.joinToString(" ")}")
        }
    }

    private fun clarifyHours(source: Player) {
        source.respond({}) {
            message("Wait for how many hours?")
            options("1", "3", "5", "10")
            command { "wait $it" }
        }
    }

    private fun wait(source: Thing, hours: Int) {
        EventManager.postEvent(StartNothingEvent(source, hours))
    }

}
