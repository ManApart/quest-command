package system.help

import core.commands.Command
import core.commands.CommandParser
import core.commands.UnknownCommand

fun isCommandGroup(args: List<String>): Boolean {
    return CommandParser.commands.asSequence().map { command -> command.getCategory().map { category -> category.lowercase() } }.contains(args)
}

fun isCommand(args: List<String>) =
        args.size == 1 && CommandParser.findCommand(args[0]) !is UnknownCommand

fun getCommandGroups(): List<String> {
    return CommandParser.commands.map { it.getCategory() }.flatten().distinct()
}

fun getCommands(group: String): List<Command> {
    val cleanGroup = group.trim().lowercase()
    return CommandParser.commands.filter {command ->
        command.getCategory().map {category ->
            category.lowercase()
        }.contains(cleanGroup)
    }
}