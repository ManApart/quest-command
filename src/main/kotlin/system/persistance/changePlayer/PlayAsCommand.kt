package system.persistance.changePlayer

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import system.persistance.createPlayer.CreateCharacterEvent

class PlayAsCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Be", "Play", "Who")
    }

    override fun getDescription(): String {
        return "Be a different character that you have unlocked."
    }

    override fun getManual(): String {
        return """
	Be ls - List characters in the current game
	Be <name> - Play as a specific character.
    Note that this doesn't work on servers. To change your server character disconnect and reconnect as that player
"""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val cleanedArgs = if (args.first() == "as") args.subList(1, args.size) else args
        val argString = cleanedArgs.joinToString(" ")
        when {
            argString == "ls" -> EventManager.postEvent(ListCharactersEvent(source))
            cleanedArgs.isEmpty() -> source.displayToMe("Please specify a character to play or use ls to list current characters.")
            else -> EventManager.postEvent(PlayAsEvent(source, cleanedArgs.joinToString(" ")))
        }
    }
}