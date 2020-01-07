package system.persistance.loading

import core.commands.Command
import core.events.EventManager
import core.history.display

class LoadCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Load", "lo")
    }

    override fun getDescription(): String {
        return "Load:\n\tLoad your game."
    }

    override fun getManual(): String {
        return "\n\tLoad - Load your game." +
                "\n\tLoad game ls - List games (which contain a world and character saves)" +
                "\n\tLoad ls - List character saves in the current game" +
                "\n\tLoad game <name> - Load a game (which contains a world and character saves)." +
                "\n\tLoad <name> - Load a specific character save."
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            argString == "game ls" -> EventManager.postEvent(ListSavesEvent(true))
            argString == "ls" -> EventManager.postEvent(ListSavesEvent(false))
            args.isEmpty() -> display("Please specify a save to load or use ls to list current saves.")
            args[0] == "game" -> EventManager.postEvent(LoadEvent(true, argString.substringAfter("game ")))
            else -> {
                EventManager.postEvent(LoadEvent(false, args.joinToString(" ")))
            }
        }
    }
}