package system.persistance.createPlayer

import core.GameState
import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import core.utility.capitalize2
import system.persistance.changePlayer.ListCharactersEvent
import system.persistance.getCharacterSaves

class CreateCharacterCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Create")
    }

    override fun getDescription(): String {
        return "Create a new character"
    }

    override fun getManual(): String {
        return """
	Create <name> - Create a new character."""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val saveNames = getCharacterSaves(GameState.gameName).map { it.lowercase() }
        val argString = args.joinToString(" ").lowercase()
        val properName = args.joinToString(" ") { it.capitalize2() }
        when {
            args.isEmpty() -> source.displayToMe("Please specify a character name or use play ls to list current characters.")
            saveNames.contains(argString) -> source.displayToMe("$properName already exists as a character.")
            else -> EventManager.postEvent(CreateCharacterEvent(source, properName))
        }
    }
}