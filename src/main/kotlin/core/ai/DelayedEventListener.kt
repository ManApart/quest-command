package core.ai

import core.events.DelayedEvent
import core.events.Event
import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class DelayedEventListener : EventListener<Event>() {
    override fun execute(event: Event) {
        if (event is DelayedEvent){
            event.source.ai.action = event
            EventManager.postEvent(GameTickEvent())
        }
    }

}