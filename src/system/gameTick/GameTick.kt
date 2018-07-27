package system.gameTick

import core.events.Event
import core.events.EventListener
import system.EventManager

class GameTick : EventListener<Event>() {
    override fun execute(event: Event) {
        if (event.usesGameTick()) {
            EventManager.postEvent(GameTickEvent())
        }
    }

}