package system.persistance.loading

import core.commands.Command
import core.events.EventManager

class LoadCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Load", "lo")
    }

    override fun getDescription(): String {
        return "Load:\n\tLoad your game."

    }

    override fun getManual(): String {
        return "\n\tLoad - Load your game X" +
                "\n\tLoad <name> - Load a specific save. X"
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        EventManager.postEvent(LoadEvent())
    }
}