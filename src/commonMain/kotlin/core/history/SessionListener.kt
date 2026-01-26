package core.history

import core.GameState
import core.GameStateKeys.AUTO_SAVE
import core.events.Event
import core.events.EventListener
import core.events.EventManager
import system.persistance.saving.SaveEvent

class SessionListener : EventListener<Event>() {
    override suspend fun complete(event: Event) {
        SessionHistory.incEventCount(event)
        if (GameState.properties.values.getBoolean(AUTO_SAVE)){
            EventManager.postEvent(SaveEvent(GameState.player))
        }
    }
}