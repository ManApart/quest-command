package commands

import kotlin.system.exitProcess

class ExitCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Exit", "Quit", "qqq")
    }

    override fun getDescription(): String {
        return "Exit the program"
    }

    override fun execute(args: List<String>) {
        print("Exiting")
        exitProcess(0)
    }
}