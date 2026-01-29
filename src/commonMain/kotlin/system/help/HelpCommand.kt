package system.help

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.commands.respond
import core.events.EventManager

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
                "\n\tHelp version - View the current commit" +
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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("All", "Commands") + getCommandGroups() + getCommandGroups().flatMap { group -> getCommands(group).map { it.name } }
            args.last() == "Commands"  -> listOf("Extended")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val argsArray = args.toTypedArray()
        when {
            args.isEmpty() && keyword == "help" -> clarifyHelp(source)
            args.isEmpty() -> EventManager.postEvent(ViewHelpEvent(source))

            args.size == 1 && args[0] == "version" -> EventManager.postEvent(ViewVersionEvent(source))

                args.size == 1 && args[0] == "commands" -> EventManager.postEvent(
                ViewHelpEvent(
                    source,
                    commandGroups = true
                )
            )
            args.size == 2 && argsArray.contentEquals(arrayOf("commands", "extended")) -> EventManager.postEvent(
                ViewHelpEvent(source, commandGroups = true, args = listOf("all"))
            )

            args.size == 2 && argsArray.contentEquals(arrayOf("command", "group")) -> clarifyCommandGroupHelp(source)

            args.size == 1 && args[0] == "command" -> clarifyCommandFromGroupHelp(source)
            args.size == 2 && args[0] == "command" -> clarifyCommandHelp(source, args[1])

            isCommand(args) -> EventManager.postEvent(
                ViewHelpEvent(
                    source,
                    commandManual = CommandParsers.findCommand(args[0])
                )
            )
            isCommandGroup(args) -> EventManager.postEvent(ViewHelpEvent(source, commandGroups = true, args = args))

            else -> EventManager.postEvent(ViewHelpEvent(source))
        }
    }

    private fun clarifyHelp(source: Player) {
        source.respond({}) {
            message("Help about what?")
            displayedOptions(
                "General Help",
                "List Commands",
                "List Commands (extended)",
                "A Command Group",
                "A Command"
            )
            options("All", "Commands", "Commands extended", "Command Group", "Command")
            command { "help $it" }
        }
    }

    private fun clarifyCommandGroupHelp(source: Player) {
        source.respond({}) {
            message("Help about which command group?")
            options(getCommandGroups())
            command { "help $it" }
        }
    }

    private fun clarifyCommandFromGroupHelp(source: Player) {
        source.respond({}) {
            message("Help about a command from which command group?")
            options(getCommandGroups())
            command { "help command $it" }
        }
    }

    private fun clarifyCommandHelp(source: Player, group: String) {
        val commands = getCommands(group)
        source.respond({ clarifyCommandFromGroupHelp(source) }) {
            message("Help about what command?")
            options(commands.map { it.name })
            command { "help $it" }
        }
    }

}