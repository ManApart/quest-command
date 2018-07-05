package gameState

import events.ArriveEvent
import events.Event
import processing.EventManager

class TriggeredEvent(private val className: String, private val params: List<String>) {

    fun execute(event: Event) {
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(GameState.world.findLocation(params[0].split(" "))))
        }
    }
}