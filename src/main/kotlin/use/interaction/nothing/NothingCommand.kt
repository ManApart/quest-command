package use.interaction.nothing

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.CommandParsers
import core.commands.ResponseRequest
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        when {
            args.isEmpty() && keyword == "Nothing" -> clarifyHours(source)
            args.isEmpty() -> wait(source.thing, 1)
            args.size == 1 && arguments.getNumber() != null -> wait(source.thing, arguments.getNumber()!!)
            else -> source.displayToMe("Unknown params for rest: ${args.joinToString(" ")}")
        }
    }

    private fun clarifyHours(source: Player) {
        val things = listOf("1", "3", "5", "10")
        val message = "Wait for how many hours?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { "wait $it" }))
    }

    private fun wait(source: Thing, hours: Int) {
        EventManager.postEvent(StartNothingEvent(source, hours))
    }

}
