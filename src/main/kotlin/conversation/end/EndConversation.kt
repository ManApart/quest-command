package conversation.end

import core.commands.CommandParser
import core.events.EventListener
import core.history.display

class EndConversation : EventListener<EndConversationEvent>() {
    override fun execute(event: EndConversationEvent) {
        CommandParser.commandInterceptor = null
        display("The conversation ends.")
    }
}