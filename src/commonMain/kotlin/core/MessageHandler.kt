package core

import core.events.EventListener
import core.history.displayToMe
import core.history.displayToOthers
import system.message.MessageEvent

class MessageHandler : EventListener<MessageEvent>() {
    override suspend fun complete(event: MessageEvent) {
        event.source.displayToMe(event.messageToYou)
        if (!event.private) {
            event.source.displayToOthers(event.messageToOthers)
        }
    }
}
