package interact.interaction.nothing

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import system.EventManager

class NothingCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Nothing", "Wait", "nn")
    }

    override fun getDescription(): String {
        return "Nothing\n\tLike resting, but less useful." +
                "\n\tNothing <duration> - Do nothing for a set amount of time."
    }

    override fun getManual(): String {
        return "\n\tNothing - Do Nothing."
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        execute(GameState.player, keyword, args)
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args)
        when {
            args.isEmpty() && keyword == "Nothing" -> clarifyHours()
            args.isEmpty() -> wait(source, 1)
            args.size == 1 && arguments.getNumber() != null -> wait(source, arguments.getNumber()!!)
            else -> display("Unknown params for rest: ${args.joinToString(" ")}")
        }
    }

    private fun clarifyHours() {
        val targets = listOf("1", "3", "5", "10")
        val message = "Wait for how many hours?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.map { it to "wait $it" }.toMap()))
    }

    private fun wait(source: Target, hours: Int) {
        if (GameState.battle == null) {
            EventManager.postEvent(NothingEvent(source, hours))
        } else {
            EventManager.postEvent(StartNothingEvent(source, hours))
        }
    }

}