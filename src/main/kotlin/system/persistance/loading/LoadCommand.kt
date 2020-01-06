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
                "\n\tLoad ls - List saves" +
                "\n\tLoad <name> - Load a specific save."
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.size == 1 && args[0] == "ls") {
            EventManager.postEvent(LoadEvent(list = true))
        } else if (args.isEmpty()) {
            display("Please specify a save to load or use ls to list current saves.")
        } else {
            EventManager.postEvent(LoadEvent(saveName = args.joinToString(" ")))
        }
    }
}