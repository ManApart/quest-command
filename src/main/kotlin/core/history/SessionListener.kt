package core.history

import core.events.Event
import core.events.EventListener
import core.AUTO_SAVE
import core.GameState
import core.GameManager

class SessionListener : EventListener<Event>() {
    override fun execute(event: Event) {
        SessionHistory.incEventCount(event)
        if (GameState.properties.values.getBoolean(AUTO_SAVE)){
            GameManager.saveGame()
        }
    }
}