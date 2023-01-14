package system.persistance.saving

import core.Player
import core.commands.Command
import core.events.EventManager

class SaveCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Save", "sa")
    }

    override fun getDescription(): String {
        return "Save your game."

    }

    override fun getManual(): String {
        return """
	Save - Save your game
	Save <name> - Save with a specific save name. X"""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        EventManager.postEvent(SaveEvent(source))
    }
}