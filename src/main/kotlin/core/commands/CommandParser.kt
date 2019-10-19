package core.commands

import core.gameState.Target
import core.history.ChatHistory
import core.utility.NameSearchableList
import core.utility.reflection.ReflectionTools
import core.utility.reflection.Reflections
import core.utility.removeFirstItem
import system.DependencyInjector
import system.EventManager
import system.activator.ActivatorParser

object CommandParser {
    private val unknownCommand = UnknownCommand()
    private var reflections = DependencyInjector.getImplementation(Reflections::class.java)
    var commands = loadCommands()
    var responseRequest: ResponseRequest? = null
    var commandSource: Target? = null

    private fun loadCommands(): NameSearchableList<Command> {
        val commands = NameSearchableList(reflections.getCommands().asSequence()
                .filter { it::class != UnknownCommand::class }
                .toList())

        commands.forEach {
            commands.addProxy(it, it.getAliases().toList())
        }

        return commands
    }

    fun reset() {
        responseRequest = null
        commandSource = null
        commands = loadCommands()
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
                val trimmedArgs = args.removeFirstItem()
                if (commandSource != null) {
                    command.execute(commandSource!!, args[0], trimmedArgs)
                } else {
                    command.execute(args[0], trimmedArgs)
                }
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
