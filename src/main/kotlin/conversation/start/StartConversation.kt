package conversation.start

import conversation.Conversation
import conversation.ConversationCommandInterceptor
import core.GameState
import core.commands.CommandParser
import core.events.EventListener
import core.history.display

class StartConversation : EventListener<StartConversationEvent>() {

    override fun execute(event: StartConversationEvent) {
        GameState.conversation = Conversation(event.speaker, event.listener)
        CommandParser.commandInterceptor = ConversationCommandInterceptor()
        display("${event.speaker} starts talking with ${event.listener}. You can end conversation by saying 'goodbye'.")
    }
}