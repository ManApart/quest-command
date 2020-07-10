package core.ai

import core.events.DelayedEvent
import core.events.Event
import core.events.EventListener

class DelayedEventListener : EventListener<Event>() {
    override fun execute(event: Event) {
        if (event is DelayedEvent){
            event.source.ai.action = event
        }
    }

}