package system.persistance.changePlayer

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe

class PlayAsCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Be", "Play")
    }

    override fun getDescription(): String {
        return "Be a different character that you have unlocked."
    }

    override fun getManual(): String {
        return """
	Be ls - List characters in the current game
	Be <name> - Play as a specific character."""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            argString == "ls" -> EventManager.postEvent(ListCharactersEvent(source))
            args.isEmpty() -> source.displayToMe("Please specify a character to play or use ls to list current characters.")
            else -> EventManager.postEvent(PlayAsEvent(source, args.joinToString(" ")))
        }
    }
}