package system.persistance.loading

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import system.persistance.getGameNames

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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("ls") + getGameNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            argString == "ls" -> EventManager.postEvent(ListSavesEvent(source))
            args.isEmpty() -> {
                source.displayToMe("Please specify a save to load.")
                EventManager.postEvent(ListSavesEvent(source))
            }

            else -> EventManager.postEvent(LoadEvent(source, args.joinToString(" ")))
        }
    }
}
