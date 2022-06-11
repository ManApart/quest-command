package core.commands

import core.GameState
import core.Player
import core.commands.CommandParsers.cleanLine
import core.events.EventManager
import core.history.GameLogger
import core.history.displayToMe
import core.utility.currentTime
import core.utility.removeFirstItem

class CommandParser(private val commandSource: Player) {
    private var responseRequest: ResponseRequest? = null
    var commandInterceptor: CommandInterceptor? = null

    fun parseInitialCommand(args: Array<String>) {
        val initialCommand = if (args.isEmpty()) {
            "help general"
        } else {
            args.joinToString(" ")
        }
        parseCommand(initialCommand)
    }

    fun parseCommand(line: String) {
        val startTime = currentTime()

        if (interceptorShouldParse(line)) {
            GameLogger.addInput(line)
            commandInterceptor!!.parseCommand(commandSource, line)
            GameLogger.endCurrent()
        } else {
            splitAndParseCommand(line)
        }

        val time = currentTime() - startTime
        GameLogger.setTimeTaken(time)
    }

    private fun interceptorShouldParse(line: String): Boolean {
        return commandInterceptor != null && commandInterceptor?.ignoredCommands()?.none { line.startsWith(it, true) } ?: false
    }

    private fun splitAndParseCommand(line: String) {
        val commands = line.split("&&")
        for (command in commands) {
            GameLogger.addInput(command)
            val responseCommand = responseRequest?.getCommand(command)
            responseRequest = null
            if (responseCommand != null) {
                parseSingleCommand(responseCommand)
            } else {
                parseSingleCommand(command)
            }
            EventManager.executeEvents()
            GameLogger.endCurrent()
        }
    }

    private fun parseSingleCommand(line: String) {
        val args: List<String> = cleanLine(line)
        if (args.isEmpty()) {
            CommandParsers.unknownCommand.execute(commandSource, listOf(line))
        } else {
            val aliasCommand = findAliasCommand(args[0])
            if (aliasCommand != null) {
                parseSingleCommand(aliasCommand)
            } else {
                val command = CommandParsers.findCommand(args[0])
                if (command == CommandParsers.unknownCommand) {
                    if (CommandParsers.castCommand.hasWord(args[0])) {
                        executeCommand(CommandParsers.castCommand, listOf("c") + args)
                    } else {
                        CommandParsers.unknownCommand.execute(commandSource, listOf(line))
                    }
                } else {
                    executeCommand(command, args)
                }
            }
        }
    }

    private fun executeCommand(command: Command, args: List<String>) {
        val trimmedArgs = args.removeFirstItem()
        command.execute(commandSource, args[0], trimmedArgs)
    }

    private fun findAliasCommand(alias: String): String? {
        return GameState.aliases[alias]
    }

    fun setResponseRequest(responseRequest: ResponseRequest?) {
        if (responseRequest != null && responseRequest.message != "") {
            commandSource.displayToMe(responseRequest.message)
        }
        this.responseRequest = responseRequest
    }

    fun getResponseRequest(): ResponseRequest? {
        return this.responseRequest
    }

}