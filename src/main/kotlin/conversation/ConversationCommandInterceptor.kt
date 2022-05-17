package conversation

import conversation.dialogue.DialogueEvent
import conversation.end.EndConversationEvent
import core.Player
import core.commands.CommandInterceptor
import core.commands.CommandParsers
import core.events.EventManager

class ConversationCommandInterceptor(val conversation: Conversation) : CommandInterceptor {

    override fun ignoredCommands() = listOf<String>()

    override fun parseCommand(source: Player, line: String) {
        val commandLine = CommandParsers.cleanLine(line).joinToString(" ")

        if (commandLine == "goodbye" || commandLine == "exit") {
            EventManager.postEvent(EndConversationEvent(source, conversation))
        } else {
            val event = DialogueEvent(source.thing, conversation, line)
            EventManager.postEvent(event)
        }
        EventManager.executeEvents()
    }

}