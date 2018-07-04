package commands

import CommandParser
import removeFirstItem

class HelpCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Help")
    }

    override fun execute(args: List<String>) {
        when {
            args.isEmpty() -> println(getDescription())
            args[0] == "commands" -> printCommands()
            else -> printManual(args[0])
        }
    }

    override fun getDescription(): String {
        return "Help: \n\tHelp Commands - Return a list of other commands that can be called." +
                "\n\tHelp <Command> - Return the manual for that command" +
                "\nNotes:" +
                "\n\tNames in brackets are params. EX: 'Travel <location>' should be typed as 'Travel Kanbara'" +
                "\n\tWords that start with a * are optional" +
                "\n\tCommands that end with X are not yet implemented"
    }

    override fun getManual(): String {
        return getDescription()
    }

    private fun printCommands() {
        var descriptions = ""
        CommandParser.commands.forEach { if (it != this) descriptions += it.getDescription() + "\n" }
        print(descriptions)
    }

    private fun printManual(commandName: String) {
        val command = CommandParser.findCommand(commandName)
        println(getTitle(command) + command.getManual())
    }

    private fun getTitle(command: Command) : String{
        val title = command.getAliases()[0]
        val aliases = removeFirstItem(command.getAliases()).joinToString(", ")
        return "$title ($aliases):"
    }
}