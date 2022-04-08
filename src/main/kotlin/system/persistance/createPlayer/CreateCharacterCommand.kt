package system.persistance.createPlayer

import core.Player
import core.commands.Command

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

    override fun execute(source: Player, keyword: String, args: List<String>) {
//        val argString = args.joinToString(" ")
//        when {
//            argString == "ls" -> EventManager.postEvent(ListCharactersEvent(source))
//            args.isEmpty() -> source.displayToMe("Please specify a character to play or use ls to list current characters.")
//            else -> EventManager.postEvent(CreateCharacterEvent(source, args.joinToString(" ")))
//        }
    }
}