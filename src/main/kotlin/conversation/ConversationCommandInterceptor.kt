package conversation

import conversation.dialogue.DialogueEvent
import conversation.end.EndConversationEvent
import core.GameState
import core.commands.CommandInterceptor
import core.commands.CommandParser
import core.events.EventManager

class ConversationCommandInterceptor : CommandInterceptor {

    override fun parseCommand(line: String) {
        val commandLine = CommandParser.cleanLine(line).joinToString(" ")

        if (commandLine == "goodbye" || commandLine == "exit") {
            EventManager.postEvent(EndConversationEvent())
        } else {
            val event = DialogueEvent(GameState.player, GameState.conversation, line)
            EventManager.postEvent(event)
        }
        EventManager.executeEvents()
    }

}