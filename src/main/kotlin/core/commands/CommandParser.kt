package core.commands

import core.DependencyInjector
import core.GameState
import core.events.EventManager
import core.history.ChatHistory
import core.history.display
import core.target.Target
import core.utility.NameSearchableList
import core.utility.removeFirstItem
import magic.castSpell.CastCommand

object CommandParser {
    private var commandsCollection = DependencyInjector.getImplementation(CommandsCollection::class)
    var commands = loadCommands()
    val unknownCommand by lazy { commands.first { it::class == UnknownCommand::class } as UnknownCommand }
    private val castCommand by lazy { commands.first { it::class == CastCommand::class } as CastCommand }
    private var responseRequest: ResponseRequest? = null
    var commandSource: Target? = null
    var commandInterceptor: CommandInterceptor? = null

    private fun loadCommands(): NameSearchableList<Command> {
        val commands = NameSearchableList(commandsCollection.values.toList())

        commands.forEach {
            commands.addProxy(it, it.getAliases().toList())
        }

        return commands
    }

    fun reset() {
        responseRequest = null
        commandSource = null
        commandInterceptor = null
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
        val startTime = System.currentTimeMillis()
        ChatHistory.addInput(line)

        if (commandInterceptor == null) {
            splitAndParseCommand(line)
        } else {
            commandInterceptor!!.parseCommand(line)
        }

        val time = System.currentTimeMillis() - startTime
        ChatHistory.getCurrent().timeTaken = time
    }

    private fun splitAndParseCommand(line: String) {
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
            val aliasCommand = findAliasCommand(args[0])
            if (aliasCommand != null) {
                parseSingleCommand(aliasCommand)
            } else {
                val command = findCommand(args[0])
                if (command == unknownCommand) {
                    if (castCommand.hasWord(args[0])) {
                        executeCommand(castCommand, listOf("c") + args)
                    } else {
                        unknownCommand.execute(listOf(line))
                    }
                } else {
                    executeCommand(command, args)
                }
            }
        }
    }

    private fun executeCommand(command: Command, args: List<String>) {
        val trimmedArgs = args.removeFirstItem()
        if (commandSource != null) {
            command.execute(commandSource!!, args[0], trimmedArgs)
        } else {
            command.execute(args[0], trimmedArgs)
        }
    }

    fun cleanLine(line: String): List<String> {
        return line.lowercase().split(" ").asSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
    }

    private fun findAliasCommand(alias: String): String? {
        return GameState.aliases[alias]
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

    fun getGroupedCommands(): Map<String, List<Command>> {
        val groups = HashMap<String, MutableList<Command>>()
        commands.forEach { command ->
            run {
                if (!groups.containsKey(command.getCategory()[0])) {
                    groups[command.getCategory()[0]] = ArrayList()
                }
                groups[command.getCategory()[0]]?.add(command)
            }
        }
        groups.forEach { entry ->
            entry.value.sortBy { it.name }
        }
        return groups.toSortedMap()
    }

    fun isPlayersTurn(): Boolean {
        return commandSource == null || commandSource == GameState.player
    }

    fun setResponseRequest(responseRequest: ResponseRequest?) {
        if (responseRequest != null && responseRequest.message != "") {
            display(responseRequest.message)
        }
        this.responseRequest = responseRequest
    }

    fun getResponseRequest(): ResponseRequest? {
        return this.responseRequest
    }

}
