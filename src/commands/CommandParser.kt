package commands

class CommandParser {
    val commands = loadCommands()
    val unknownCommand = UnknownCommand()

    private fun loadCommands(): Array<Command> {
        val commands = mutableListOf<Command>()
        commands.add(HelpCommand())
        commands.add(ExitCommand())

        return commands.toTypedArray()
    }

    fun parseCommand(line: String) {
        val args = line.split(" ")
        if (args.isEmpty()) {
            unknownCommand.execute(listOf(line))
        }
        val command = findCommand(args[0])
        command.execute(args)
    }

    private fun findCommand(alias: String): Command {
        //TODO - Ignore case contains
        return commands.firstOrNull { it.getAliases().contains(alias)} ?: unknownCommand
    }
}