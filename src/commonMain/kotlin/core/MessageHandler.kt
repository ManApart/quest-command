package core

import core.events.EventListener
import core.history.display
import system.message.MessageEvent

class MessageHandler : EventListener<MessageEvent>() {
    override fun execute(event: MessageEvent) {
        event.source.display(event.message)
    }

}