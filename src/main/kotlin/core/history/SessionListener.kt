package core.history

import core.events.Event
import core.events.EventListener

class SessionListener : EventListener<Event>() {
    override fun execute(event: Event) {
        SessionHistory.incEventCount(event)
    }
}