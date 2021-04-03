package system.persistance.loading

import core.commands.Command
import core.events.EventManager
import core.history.display

class LoadCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Load", "lo")
    }

    override fun getDescription(): String {
        return "Load your game."
    }

    override fun getManual(): String {
        return """
	Load - Load your game.
	Load ls - List games (which contain a world and character saves)
	Load <name> - Load a game (which contains a world and character saves)."""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            argString == "ls" -> EventManager.postEvent(ListSavesEvent())
            args.isEmpty() -> display("Please specify a save to load or use ls to list current saves.")
            else -> EventManager.postEvent(LoadEvent(args.joinToString(" ")))
        }
    }
}