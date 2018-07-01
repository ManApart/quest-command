import commands.*

class CommandParser {
    private val commands = loadCommands()
    private val unknownCommand = UnknownCommand()
    private val filteredWords = listOf("to", "with")

    private fun loadCommands(): List<Command> {
        val commands = mutableListOf<Command>()
        val help = HelpCommand()
        commands.add(help)
        commands.add(ExitCommand())
        commands.add(TravelCommand())

        val commandList = commands.toList()
        help.setCommands(commandList)
        return commandList
    }

    fun parseCommand(line: String) {
        val args: List<String> = cleanLine(line)
        if (args.isEmpty()) {
            unknownCommand.execute(listOf(line))
        } else {

            val command = findCommand(args[0])

            val trimmedArgs = if (args.size > 1) args.subList(1, args.size) else listOf()
            command.execute(trimmedArgs)
        }
    }

    private fun cleanLine(line: String) : List<String>{
        var cleanedString = line.toLowerCase()
        filteredWords.forEach { cleanedString = cleanedString.replace(it, "") }
        return  cleanedString.split(" ").map { it.trim()}.filter { it.isNotEmpty() }
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