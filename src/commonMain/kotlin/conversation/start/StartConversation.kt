package conversation.start

import conversation.Conversation
import conversation.ConversationCommandInterceptor
import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.display
import core.history.displayToMe

class StartConversation : EventListener<StartConversationEvent>() {

    override suspend fun complete(event: StartConversationEvent) {
        val interceptor = ConversationCommandInterceptor(Conversation(event.speaker.thing, event.listener))

        CommandParsers.getParser(event.speaker).commandInterceptor = interceptor
        GameState.getPlayer(event.listener)?.let { listener ->
            CommandParsers.getParser(listener).commandInterceptor = interceptor
        }

        event.speaker.display("${event.speaker.thing.name} starts talking with ${event.listener.name}.")
        event.speaker.displayToMe("The conversation can be ended by saying 'goodbye'.")
        event.listener.displayToMe("The conversation can be ended by saying 'goodbye'.")
    }
}