package core.events.multiEvent

import core.events.EventListener
import core.events.EventManager

class MultiEventListener : EventListener<MultiEvent>() {
    override suspend fun complete(event: MultiEvent) {
        event.events.forEach {
            EventManager.postEvent(it)
        }
    }
}