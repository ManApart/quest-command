package status.rest

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.respond
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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("1", "5", "10")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args)
        when {
            args.isEmpty() && keyword == "rest" -> clarifyHours(source)
            args.isEmpty() -> rest(source.thing, 1)
            args.size == 1 && arguments.getNumber() != null -> rest(source.thing, arguments.getNumber()!!)
            else -> source.displayToMe("Unknown params for rest: ${args.joinToString(" ")}")
        }
    }

    private fun clarifyHours(source: Player) {
        source.respond({}) {
            message("Rest for how many hours?")
            options("1", "3", "5", "10")
            command { "rest $it" }
        }
    }

    private fun rest(source: Thing, hours: Int) {
        EventManager.postEvent(RestEvent(source, hours))
    }
}


