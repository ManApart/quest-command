package system.help

import core.commands.Command
import core.commands.CommandParser
import core.commands.UnknownCommand
import core.history.display
import system.EventManager

class HelpCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Help")
    }

    override fun getDescription(): String {
        return "Help: \n\tHelp Commands - Return a list of other types of commands that can be called." +
                "\n\tHelp Commands all - Return a list of commands and all their aliases." +
                "\n\tHelp <Command Group> - Return the list of commands within a group of commands" +
                "\n\tHelp <Command> - Return the manual for that command" +
                "\nNotes:" +
                "\n\tNames in brackets are params. EX: 'Travel <location>' should be typed as 'Travel Kanbara'" +
                "\n\tWords that start with a * are optional" +
                "\n\tCommands that end with X are not yet implemented"
    }

    override fun getManual(): String {
        return getDescription()
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> EventManager.postEvent(ViewHelpEvent())
            args.size == 2 && args[0] == "commands" && args[1] == "all" -> EventManager.postEvent(ViewHelpEvent(commandGroups = true, args= listOf("all")))
            args[0] == "commands" -> EventManager.postEvent(ViewHelpEvent(commandGroups = true))
            isCommand(args) -> EventManager.postEvent(ViewHelpEvent(commandManual = CommandParser.findCommand(args[0])))
            isCommandGroup(args) -> EventManager.postEvent(ViewHelpEvent(commandGroups = true, args = args))
        }
    }

    private fun isCommandGroup(args: List<String>): Boolean {
        return CommandParser.commands.asSequence().map { command -> command.getCategory().map { category -> category.toLowerCase() } }.contains(args)
    }

    private fun isCommand(args: List<String>) =
            args.size == 1 && CommandParser.findCommand(args[0]) !is UnknownCommand

}