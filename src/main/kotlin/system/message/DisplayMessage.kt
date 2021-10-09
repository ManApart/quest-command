package system.message

import core.events.EventListener
import core.history.display
import core.history.displayYou

class DisplayMessage : EventListener<DisplayMessageEvent>() {
    override fun execute(event: DisplayMessageEvent) {
        event.listener.displayYou(event.message)
    }
}