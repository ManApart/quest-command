package status.rest

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.CommandParsers
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

class RestCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Rest", "Sleep", "Camp", "rs")
    }

    override fun getDescription(): String {
        return "Rest and regain your stamina."
    }

    override fun getManual(): String {
        return """
	Rest - Rest for an hour.
	Rest <duration> - Rest for a set amount of time."""
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        when {
            args.isEmpty() && keyword == "rest" -> clarifyHours(source)
            args.isEmpty() -> rest(source.thing, 1)
            args.size == 1 && arguments.getNumber() != null -> rest(source.thing, arguments.getNumber()!!)
            else -> source.displayToMe("Unknown params for rest: ${args.joinToString(" ")}")
        }
    }

    private fun clarifyHours(source: Player) {
        val things = listOf("1", "3", "5", "10")
        val message = "Rest for how many hours?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source, ResponseRequest(message, things.associateWith { "rest $it" }))
    }

    private fun rest(source: Thing, hours: Int) {
        EventManager.postEvent(RestEvent(source, hours))
    }
}


