package conversation

import conversation.end.EndConversationEvent
import core.commands.CommandInterceptor
import core.commands.CommandParser
import core.events.EventManager
import core.target.Target

class ConversationCommandInterceptor(private val speaker: Target, private val listener: Target) : CommandInterceptor {
    override fun parseCommand(line: String) {
        val command = CommandParser.cleanLine(line)
        val commandLine = command.joinToString(" ")
        if (commandLine == "goodbye" || commandLine == "exit") {
            EventManager.postEvent(EndConversationEvent())
        } else {
            println("Said: $command")
        }
        EventManager.executeEvents()
    }
}