package system

import core.commands.Command
import core.history.displayGlobal
import core.thing.Thing
import kotlin.system.exitProcess

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

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        //TODO - move to event / system manager
        displayGlobal("Exiting")
        exitProcess(0)
    }
}