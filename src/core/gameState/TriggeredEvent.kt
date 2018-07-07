package core.gameState

import travel.ArriveEvent
import core.events.Event
import system.EventManager

class TriggeredEvent(private val className: String, private val params: List<String>) {

    fun execute(event: Event) {
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(destination = GameState.world.findLocation(params[0].split(" "))))
        }
    }
}