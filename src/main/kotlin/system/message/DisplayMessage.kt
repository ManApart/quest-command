package system.message

import core.events.EventListener
import core.history.display

class DisplayMessage : EventListener<DisplayMessageEvent>() {
    override fun execute(event: DisplayMessageEvent) {
        display(event.message)
    }
}