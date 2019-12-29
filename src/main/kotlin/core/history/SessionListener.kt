package core.history

import core.AUTO_SAVE
import core.GameState
import core.events.Event
import core.events.EventListener
import core.events.EventManager
import system.persistance.saving.SaveEvent

class SessionListener : EventListener<Event>() {
    override fun execute(event: Event) {
        SessionHistory.incEventCount(event)
        if (GameState.properties.values.getBoolean(AUTO_SAVE)){
            EventManager.postEvent(SaveEvent())
        }
    }
}