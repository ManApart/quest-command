package system.message

import core.events.EventListener
import core.history.displayToMe

class DisplayMessage : EventListener<DisplayMessageEvent>() {
    override suspend fun execute(event: DisplayMessageEvent) {
        event.listener.displayToMe(event.message)
    }
}