package core.commands

import core.history.ChatHistory
import core.utility.NameSearchableList
import core.utility.reflection.ReflectionTools
import core.utility.reflection.Reflections
import system.DependencyInjector
import system.EventManager
import system.activator.ActivatorParser

object CommandParser {
    private val unknownCommand = UnknownCommand()
    private var reflections = DependencyInjector.getImplementation(Reflections::class.java)
    val commands by lazy { loadCommands() }
    var responseRequest: ResponseRequest? = null

    private fun loadCommands(): NameSearchableList<Command> {
        val commands = NameSearchableList(reflections.getCommands().asSequence()
                .filter { it::class != UnknownCommand::class }
                .toList())

        commands.forEach {
            commands.addProxy(it, it.getAliases().toList())
        }

        return commands
    }

    fun parseInitialCommand(args: Array<String>) {
        val initialCommand = if (args.isEmpty()) {
            "look all"
        } else {
            args.joinToString(" ")
        }
        parseCommand(initialCommand)
    }

    fun parseCommand(line: String) {
        ChatHistory.addInput(line)
        val commands = line.split("&&")
        for (command in commands) {
            val responseCommand = responseRequest?.getCommand(command)
            responseRequest = null
            if (responseCommand != null) {
                parseSingleCommand(responseCommand)
            } else {
                parseSingleCommand(command)
            }
            EventManager.executeEvents()
        }
    }

    private fun parseSingleCommand(line: String) {
        val args: List<String> = cleanLine(line)
        if (args.isEmpty()) {
            unknownCommand.execute(listOf(line))
        } else {
            val command = findCommand(args[0])
            if (command == unknownCommand) {
                unknownCommand.execute(listOf(line))
            } else {
                val trimmedArgs = removeFirstItem(args)
                command.execute(args[0], trimmedArgs)
            }
        }
    }

    private fun cleanLine(line: String): List<String> {
        return line.toLowerCase().split(" ").asSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
    }

    fun findCommand(alias: String): Command {
        return commands.getOrNull(alias) ?: unknownCommand
    }

    inline fun <reified C : Command> getCommand(): C {
        return commands.first { it is C } as C
    }

    fun getCategories(): List<String> {
        val categories = mutableListOf<String>()
        commands.flatMap { it.getCategory() }.forEach {
            if (!categories.contains(it)) {
                categories.add(it)
            }
        }
        return categories
    }

}

//TODO -move to args parser?
fun removeFirstItem(list: List<String>): List<String> {
    return if (list.size > 1) list.subList(1, list.size) else listOf()
}

fun removeFirstItem(list: Array<String>): Array<String> {
    return if (list.size > 1) list.toList().subList(1, list.size).toTypedArray() else arrayOf()
}
