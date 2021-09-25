package system.persistance.saving

import core.commands.Command
import core.events.EventManager
import core.target.Target

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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        EventManager.postEvent(SaveEvent())
    }
}