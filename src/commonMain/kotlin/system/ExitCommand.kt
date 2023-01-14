package system

import core.Player
import core.commands.Command
import core.history.displayGlobal
import core.thing.Thing
import core.utility.exit

class ExitCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Exit", "Quit", "qqq")
    }

    override fun getDescription(): String {
        return "Exit the program."
    }

    override fun getManual(): String {
        return """
	Exit - Exit the program."""
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    override suspend fun execute(source: Thing, keyword: String, args: List<String>) {
        //TODO - move to event / system manager
        displayGlobal("Exiting")
        exit()
    }
}