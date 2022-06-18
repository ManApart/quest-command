package system.help

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.commands.respond

class CommandsCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("Commands")
    }

    override fun getDescription(): String {
        return "Commands: \n\tCommands <Group> <Command> - Execute a command."
    }

    override fun getManual(): String {
        return getDescription()
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> clarifyCommandGroup(source)
            args.size == 1 && isCommand(args) -> CommandParsers.parseCommand(source, args[1])
            args.size == 1 && isCommandGroup(args) -> clarifyCommand(source, args[0])
            args.size == 2 -> CommandParsers.parseCommand(source, args[2])
        }
    }

    private fun clarifyCommandGroup(source: Player) {
        source.respond({}) {
            options(getCommandGroups())
            command { "commands $it" }
        }
    }

    private fun clarifyCommand(source: Player, group: String) {
        source.respond({}) {
            optionsNamed(getCommands(group))
        }
    }


}