package system

import core.commands.Command
import core.history.display
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

    override fun execute(keyword: String, args: List<String>) {
        //TODO - move to event / system manager
        display("Exiting")
        exitProcess(0)
    }
}