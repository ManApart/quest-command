package conversation.start

import conversation.Conversation
import conversation.ConversationCommandInterceptor
import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.display

class StartConversation : EventListener<StartConversationEvent>() {

    override fun execute(event: StartConversationEvent) {
        GameState.conversation = Conversation(event.speaker.thing, event.listener)
        CommandParsers.getParser(event.speaker).commandInterceptor  = ConversationCommandInterceptor()
        event.speaker.display("${event.speaker.thing.name} starts talking with ${event.listener.name}. You can end conversation by saying 'goodbye'.")
    }
}