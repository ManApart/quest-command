package system.help

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest

class CommandsCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Commands")
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
        val response = ResponseRequest(groups.map { it to "commands $it" }.toMap())
        CommandParser.setResponseRequest(response)
    }

    private fun clarifyCommand(group: String) {
        val commands = getCommands(group).map { it.name }
        val response = ResponseRequest(commands.map { it to it }.toMap())
         CommandParser.setResponseRequest(response)
    }


}