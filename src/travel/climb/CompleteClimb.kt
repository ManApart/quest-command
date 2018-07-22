package travel.climb

import core.events.EventListener
import core.gameState.GameState
import core.gameState.consume
import core.gameState.stat.Stat
import status.ExpGainedEvent
import system.EventManager
import travel.ArriveEvent

class CompleteClimb : EventListener<ClimbCompleteEvent>() {
    override fun shouldExecute(event: ClimbCompleteEvent): Boolean {
        return event.creature == GameState.player.creature
    }

    override fun execute(event: ClimbCompleteEvent) {
        event.target.consume(event)
        GameState.journey = null
        if (GameState.player.creature.location == event.destination) {
            println("You climb back off ${event.target.name}")
        } else {
            val distance = event.origin.position.getDistanceZ(event.destination.position)
            EventManager.postEvent(ExpGainedEvent(event.creature, Stat.CLIMBING, distance))
            EventManager.postEvent(ArriveEvent(event.creature, event.origin, event.destination, "Climb"))
        }
    }
}