package conversation.start

import conversation.ConversationCommandInterceptor
import core.commands.CommandParser
import core.events.EventListener
import core.history.display

class StartConversation : EventListener<StartConversationEvent>() {

    override fun execute(event: StartConversationEvent) {
        CommandParser.commandInterceptor = ConversationCommandInterceptor(event.speaker, event.listener)
        display("${event.speaker} starts talking with ${event.listener}. You can end conversation by saying 'goodbye'.")

    }
}