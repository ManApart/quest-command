package system.help

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest

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

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> clarifyCommandGroup()
            args.size == 1 && isCommand(args) -> CommandParser.parseCommand(args[1])
            args.size == 1 && isCommandGroup(args) -> clarifyCommand(args[0])
            args.size == 2 -> CommandParser.parseCommand(args[2])
        }
    }

    private fun clarifyCommandGroup() {
        val groups = getCommandGroups()
        val response = ResponseRequest(groups.associateWith { "commands $it" })
        CommandParser.setResponseRequest(response)
    }

    private fun clarifyCommand(group: String) {
        val commands = getCommands(group).map { it.name }
        val response = ResponseRequest(commands.associateWith { it })
         CommandParser.setResponseRequest(response)
    }


}