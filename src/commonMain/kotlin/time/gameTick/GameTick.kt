package time.gameTick

import core.events.Event
import core.events.EventListener
import core.events.EventManager

class GameTick : EventListener<Event>() {
    override suspend fun complete(event: Event) {
        if (event.gameTicks() > 0) {
            EventManager.postEvent(GameTickEvent(event.gameTicks()))
        }
    }

}