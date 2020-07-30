package conversation

import conversation.end.EndConversationEvent
import conversation.parsing.SentenceParser
import core.commands.CommandInterceptor
import core.commands.CommandParser
import core.events.EventManager
import core.target.Target

class ConversationCommandInterceptor(private val speaker: Target, private val listener: Target) : CommandInterceptor {
    override fun parseCommand(line: String) {
        val commandLine = CommandParser.cleanLine(line).joinToString(" ")

        if (commandLine == "goodbye" || commandLine == "exit" ){
            EventManager.postEvent(EndConversationEvent())
        } else {
            val event = SentenceParser(speaker, listener, commandLine).event
            if (event != null){
                EventManager.postEvent(event)
            }
        }
        EventManager.executeEvents()
    }

}