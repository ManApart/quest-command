package system.persistance.newGame

import core.commands.Command
import core.events.EventManager
import core.history.display

class CreateNewGameCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("New", "ng")
    }

    override fun getDescription(): String {
        return "Create a new game."

    }

    override fun getManual(): String {
        return """
	New <name> - Create a new game with a specific name"""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        val saveName = args.joinToString(" ")
        if (saveName.isBlank()) {
            display("You must give a name for the new game.")
        } else {
            EventManager.postEvent(CreateNewGameEvent(saveName))
        }
    }
}