package status.rest

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.GameState
import core.history.display
import core.events.EventManager

class RestCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Rest", "Sleep", "Camp", "rs")
    }

    override fun getDescription(): String {
        return "Rest:\n\tRest and regain your stamina."
    }

    override fun getManual(): String {
        return "\n\tRest - Rest for an hour." +
                "\n\tRest <duration> - Rest for a set amount of time."
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (!GameState.player.canRest) {
            display("You can't rest right now!")
        } else {
            val arguments = Args(args)
            when {
                args.isEmpty() && keyword == "rest" -> clarifyHours()
                args.isEmpty() -> rest(1)
                args.size == 1 && arguments.getNumber() != null -> rest(arguments.getNumber()!!)
                else -> display("Unknown params for rest: ${args.joinToString(" ")}")
            }
        }
    }

    private fun clarifyHours() {
        val targets = listOf("1", "3", "5", "10")
        val message = "Rest for how many hours?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.map { it to "rest $it" }.toMap()))
    }

    private fun rest(hours: Int) {
        EventManager.postEvent(RestEvent(GameState.player, hours))
    }
}


