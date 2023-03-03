package conversation.end

import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.display
import core.history.displayToMe

class EndConversation : EventListener<EndConversationEvent>() {
    override suspend fun complete(event: EndConversationEvent) {
        val speaker = event.conversation.getLatestSpeaker()
        val listener = event.conversation.getLatestListener()

        GameState.getPlayer(speaker)?.let { player ->
            CommandParsers.getParser(player).commandInterceptor = null
        }
        GameState.getPlayer(listener)?.let { player ->
            CommandParsers.getParser(player).commandInterceptor = null
        }

        speaker.displayToMe("The conversation ends.")
        listener.displayToMe("The conversation ends.")
    }
}