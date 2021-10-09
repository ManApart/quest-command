package conversation

import conversation.dialogue.DialogueEvent
import conversation.end.EndConversationEvent
import core.GameState
import core.commands.CommandInterceptor
import core.commands.CommandParser
import core.events.EventManager
import core.target.Target

class ConversationCommandInterceptor : CommandInterceptor {

    override fun parseCommand(source: Target, line: String) {
        val commandLine = CommandParser.cleanLine(line).joinToString(" ")

        if (commandLine == "goodbye" || commandLine == "exit") {
            EventManager.postEvent(EndConversationEvent(source))
        } else {
            val event = DialogueEvent(source, GameState.conversation, line)
            EventManager.postEvent(event)
        }
        EventManager.executeEvents()
    }

}