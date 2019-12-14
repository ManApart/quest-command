package time.gameTick

import core.events.Event
import core.events.EventListener
import core.events.EventManager

class GameTick : EventListener<Event>() {
    override fun execute(event: Event) {
        if (event.gameTicks() > 0) {
            EventManager.postEvent(GameTickEvent(event.gameTicks()))
        }
    }

}