package commands

class CommandParser {
    val commands = loadCommands()
    val unknownCommand = UnknownCommand()

    private fun loadCommands(): Array<Command> {
        val commands = mutableListOf<Command>()
        commands.add(HelpCommand())

        return commands.toTypedArray()
    }

    fun parseCommand(line: String) {
        val args = line.split(" ")
        if (args.isEmpty()) {
            unknownCommand.execute(arrayOf(line))
        }
        val command = findCommand(args[0])
    }

    private fun findCommand(alias: String): Command {
        return commands.firstOrNull { it.getAliases().contains(alias)} ?: unknownCommand
    }
}