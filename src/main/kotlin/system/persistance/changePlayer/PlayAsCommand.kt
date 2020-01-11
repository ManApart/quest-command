package system.persistance.changePlayer

import core.commands.Command
import core.events.EventManager
import core.history.display

class PlayAsCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Be", "Play")
    }

    override fun getDescription(): String {
        return "Be:\n\tBe a different character that you have unlocked."
    }

    override fun getManual(): String {
        return "\n\tBe ls - List characters in the current game" +
                "\n\tBe <name> - Play as a specific character."
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            argString == "ls" -> EventManager.postEvent(ListCharactersEvent())
            args.isEmpty() -> display("Please specify a character to play or use ls to list current characters.")
            else -> EventManager.postEvent(PlayAsEvent(args.joinToString(" ")))
        }
    }
}