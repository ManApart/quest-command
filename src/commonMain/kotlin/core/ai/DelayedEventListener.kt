package core.ai

import core.events.DelayedEvent
import core.events.Event
import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class DelayedEventListener : EventListener<Event>() {
    override suspend fun execute(event: Event) {
        if (event is DelayedEvent){
            event.source.mind.ai.action = event
            EventManager.postEvent(GameTickEvent())
        }
    }

}