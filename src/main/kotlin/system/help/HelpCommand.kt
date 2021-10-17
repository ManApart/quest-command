package system.help

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.thing.Thing

class HelpCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("Help", "h")
    }

    override fun getDescription(): String {
        return "Help: \n\tHelp All - Return general help" +
                "\n\tHelp Commands - Return a list of other types of commands that can be called." +
                "\n\tHelp Commands extended - Return a list of commands and all their aliases." +
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

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val argsArray = args.toTypedArray()
        when {
            args.isEmpty() && keyword == "help" -> clarifyHelp()
            args.isEmpty() -> EventManager.postEvent(ViewHelpEvent(source))

            args.size == 1 && args[0] == "commands" -> EventManager.postEvent(ViewHelpEvent(source, commandGroups = true))
            args.size == 2 && argsArray.contentEquals(arrayOf("commands", "extended")) -> EventManager.postEvent(ViewHelpEvent(source, commandGroups = true, args = listOf("all")))

            args.size == 2 && argsArray.contentEquals(arrayOf("command", "group")) -> clarifyCommandGroupHelp()

            args.size == 1 && args[0] == "command" -> clarifyCommandFromGroupHelp()
            args.size == 2 && args[0] == "command" -> clarifyCommandHelp(args[1])

            isCommand(args) -> EventManager.postEvent(ViewHelpEvent(source, commandManual = CommandParser.findCommand(args[0])))
            isCommandGroup(args) -> EventManager.postEvent(ViewHelpEvent(source, commandGroups = true, args = args))

            else -> EventManager.postEvent(ViewHelpEvent(source))
        }
    }

    private fun clarifyHelp() {
        val things = listOf("General Help", "List Commands", "List Commands (extended)", "A Command Group", "A Command")
        val commands = listOf("All", "Commands", "Commands extended", "Command Group", "Command").map { "help $it" }

        val message = "Help about what?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest.new(message, things, commands))
    }

    private fun clarifyCommandGroupHelp() {
        val things = getCommandGroups()
        val message = "Help about which command group?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "help $it" }))
    }

    private fun clarifyCommandFromGroupHelp() {
        val things = getCommandGroups()
        val message = "Help about a command from which command group?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "help command $it" }))
    }

    private fun clarifyCommandHelp(group: String) {
        val things = getCommands(group).map { it.name }
        val message = "Help about what command?\n\t${things.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, things.associateWith { "help $it" }))
    }

}