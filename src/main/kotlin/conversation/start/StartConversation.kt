package conversation.start

import conversation.Conversation
import conversation.ConversationCommandInterceptor
import core.commands.CommandParsers
import core.events.EventListener
import core.history.display

class StartConversation : EventListener<StartConversationEvent>() {

    override fun execute(event: StartConversationEvent) {
        CommandParsers.getParser(event.speaker).commandInterceptor  = ConversationCommandInterceptor(Conversation(event.speaker.thing, event.listener))
        event.speaker.display("${event.speaker.thing.name} starts talking with ${event.listener.name}. You can end conversation by saying 'goodbye'.")
    }
}