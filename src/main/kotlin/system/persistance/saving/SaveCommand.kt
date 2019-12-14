package system.persistance.saving

import core.commands.Command
import core.events.EventManager

class SaveCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Save", "sa")
    }

    override fun getDescription(): String {
        return "Save:\n\tSave your game."

    }

    override fun getManual(): String {
        return "\n\tSave - Save your game X" +
                "\n\tSave <name> - Save with a specific save name. X"
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        EventManager.postEvent(SaveEvent())
    }
}