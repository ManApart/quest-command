package travel.climb

import core.events.EventListener
import core.gameState.GameState
import core.history.display
import system.EventManager
import travel.ArriveEvent

class ClimbComplete : EventListener<ClimbCompleteEvent>() {
    override fun shouldExecute(event: ClimbCompleteEvent): Boolean {
        return event.creature == GameState.player
    }

    override fun execute(event: ClimbCompleteEvent) {
        event.target.consume(event)
        val climbBackOff = event.destination == GameState.player.climbTarget?.location

        if (climbBackOff) {
            display("You climb back off ${event.target.name}")
        }

        EventManager.postEvent(ArriveEvent(event.creature, event.origin, event.destination, "Climb", climbBackOff))
        GameState.player.finishClimbing()
    }
}