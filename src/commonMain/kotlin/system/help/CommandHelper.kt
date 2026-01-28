package system.help

import core.commands.Command
import core.commands.CommandParsers
import core.commands.UnknownCommand

fun isCommandGroup(args: List<String>): Boolean {
    return CommandParsers.commands.asSequence().map { command -> command.getCategory().map { category -> category.lowercase() } }.contains(args)
}

fun isCommand(args: List<String>) =
        args.size == 1 && CommandParsers.findCommand(args[0]) !is UnknownCommand

fun getCommandGroups(): List<String> {
    return CommandParsers.commands.flatMap { it.getCategory() }.distinct().filterNot { it.isBlank() }
}

fun getCommands(group: String): List<Command> {
    val cleanGroup = group.trim().lowercase()
    return CommandParsers.commands.filter {command ->
        command.getCategory().map {category ->
            category.lowercase()
        }.contains(cleanGroup)
    }
}