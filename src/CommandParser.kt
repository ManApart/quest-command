import commands.*

class CommandParser {
    private val commands = loadCommands()
    private val unknownCommand = UnknownCommand()

    private fun loadCommands(): Array<Command> {
        val commands = mutableListOf<Command>()
        commands.add(HelpCommand())
        commands.add(ExitCommand())
        commands.add(TravelCommand())

        return commands.toTypedArray()
    }

    fun parseCommand(line: String) {
        val args: List<String> = line.split(" ").map{ it.toLowerCase().trim()}
        if (args.isEmpty()) {
            unknownCommand.execute(listOf(line))
        }
        val command = findCommand(args[0])

        val trimmedArgs = if (args.size > 1) {
            args.subList(1, args.size - 1)
        } else {
            listOf()
        }
        command.execute(trimmedArgs)
    }

    private fun findCommand(alias: String): Command {
        return commands.firstOrNull { containsIgnoreCase(it.getAliases(), alias) } ?: unknownCommand
    }

    private fun containsIgnoreCase(textList: Array<String>, matchText: String): Boolean {
        val match = matchText.toLowerCase().trim()
        textList.forEach {
            if (it.toLowerCase().trim() == match) {
                return true
            }
        }
        return false
    }
}