package conversation.end

import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.display

class EndConversation : EventListener<EndConversationEvent>() {
    override fun execute(event: EndConversationEvent) {
        CommandParsers.getParser(event.source).commandInterceptor = null
        GameState.getConversation(event.source.thing)?.let { GameState.conversations.remove(it) }
        event.source.display("The conversation ends.")
    }
}