package travel.climb

import core.events.EventListener
import core.gameState.GameState
import core.history.display
import system.EventManager
import travel.ArriveEvent

class ClimbComplete : EventListener<ClimbCompleteEvent>() {
    override fun shouldExecute(event: ClimbCompleteEvent): Boolean {
        return event.creature == GameState.player.creature
    }

    override fun execute(event: ClimbCompleteEvent) {
        event.target.consume(event)
        GameState.player.climbJourney = null
        if (GameState.player.creature.location == event.destination) {
            display("You climb back off ${event.target.name}")
        } else {
            EventManager.postEvent(ArriveEvent(event.creature, event.origin, event.destination, "Climb"))
        }
        GameState.player.canRest = true
        GameState.player.canTravel = true
        GameState.player.canInteract = true
    }
}