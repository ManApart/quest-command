package conversation

import conversation.end.EndConversationEvent
import conversation.dialogue.DialogueEvent
import core.commands.CommandInterceptor
import core.commands.CommandParser
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.NameSearchableList
import core.utility.Named
import traveling.location.location.NOWHERE_NODE

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