package system

import core.commands.Command
import core.commands.CommandParser
import core.commands.UnknownCommand
import core.commands.removeFirstItem

class HelpCommand : Command() {

    override fun getAliases(): Array<String> {
        return arrayOf("Help")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> println(getDescription())
            args[0] == "commands" -> printCommandGroups()
            else -> printTopic(args)
        }
    }

    override fun getDescription(): String {
        return "Help: \n\tHelp Commands - Return a list of other types of commands that can be called." +
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

    private fun printCommandGroups() {
        val groups = HashMap<String, MutableList<String>>()
        CommandParser.commands.forEach { command ->
            run {
                if (!groups.containsKey(command.getCategory()[0])) {
                    groups[command.getCategory()[0]] = ArrayList()
                }
                groups[command.getCategory()[0]]?.add(command.getAliases()[0])
            }
        }
        var groupList = ""
        groups.toSortedMap().forEach {
            it.value.sort()
            groupList += "${it.key}:\n\t${it.value.joinToString(", ")}\n"
        }
        println("Help <Group Name> to learn about one of the following groups:\n$groupList")
    }

    private fun printTopic(args: List<String>) {
        if (args.size == 1 && CommandParser.findCommand(args[0]) !is UnknownCommand) {
            printManual(args[0])
        } else if (isCommandGroup(args)) {
            printCommandGroup(args)
        } else {
            println(getDescription())
        }
    }

    private fun isCommandGroup(args: List<String>): Boolean {
        return CommandParser.commands.asSequence().map { command -> command.getCategory().map { category -> category.toLowerCase() } }.contains(args)
    }

    private fun printCommandGroup(args: List<String>) {
        var description = "Help <Command> to learn more about on of the following topics:\n"
        //TODO - handle sub-categories
        //TODO - sort alphabetically
        CommandParser.commands.forEach { command ->
            if (command.getCategory().map { it.toLowerCase() }.toTypedArray() contentEquals args.toTypedArray()) {
                description += command.getDescription() + "\n"
            }
        }
        println(description)
    }

    private fun printManual(commandName: String) {
        val command = CommandParser.findCommand(commandName)
        println(getTitle(command) + command.getManual())
    }

    private fun getTitle(command: Command): String {
        val title = command.getAliases()[0]
        val aliases = removeFirstItem(command.getAliases()).joinToString(", ")
        return "$title ($aliases):"
    }
}