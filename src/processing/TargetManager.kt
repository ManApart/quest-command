package processing

import events.Event
import events.EventListener

object TargetManager {
    class EventHandler : EventListener<Event>() {
        override fun execute(event: Event) {
            println("Event: $event")
        }
    }
}