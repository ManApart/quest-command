package conversation

import conversation.dialogue.DialogueEvent
import conversation.end.EndConversationEvent
import core.GameState
import core.Player
import core.commands.CommandInterceptor
import core.commands.CommandParsers
import core.events.EventManager

class ConversationCommandInterceptor : CommandInterceptor {

    override fun parseCommand(source: Player, line: String) {
        val commandLine = CommandParsers.cleanLine(line).joinToString(" ")

        if (commandLine == "goodbye" || commandLine == "exit") {
            EventManager.postEvent(EndConversationEvent(source))
        } else {
            val event = DialogueEvent(source, GameState.conversation, line)
            EventManager.postEvent(event)
        }
        EventManager.executeEvents()
    }

}