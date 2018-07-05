package processing

import events.Event
import events.EventListener

object TargetManager {
    class EventHandler : EventListener<Event>() {
        override fun handle(event: Event) {
            println("Event: $event")
        }
    }
}