package travel.climb

import core.events.EventListener
import core.gameState.GameState
import core.gameState.consume
import system.EventManager
import travel.ArriveEvent

class CompleteClimb : EventListener<ClimbCompleteEvent>() {
    override fun shouldExecute(event: ClimbCompleteEvent): Boolean {
        return event.creature == GameState.player
    }

    override fun execute(event: ClimbCompleteEvent) {
        event.target.consume(event)
        GameState.journey = null
        if (GameState.player.location == event.origin) {
            println("You climb back off ${event.target}")
        } else {
            EventManager.postEvent(ArriveEvent(event.creature, event.origin, event.destination, "Climb"))
        }
    }
}