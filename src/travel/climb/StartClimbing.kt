package travel.climb

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import system.EventManager

class StartClimbing : EventListener<StartClimbingEvent>() {
    override fun shouldExecute(event: StartClimbingEvent): Boolean {
        return event.creature == GameState.player.creature && event.target is Activator && event.target.climb != null
    }

    override fun execute(event: StartClimbingEvent) {
        val climb = (event.target as Activator).climb
        if (climb != null) {
            val path = ClimbPathManager.getPath(climb.name)
GameState.journey = ClimbJourney(event.target, GameState.player.creature.location, climb.destination, climb.upwards, path)
            EventManager.postEvent(ClimbJourneyEvent(path.getStart(climb.upwards), event.force))
        }
        GameState.player.canRest = false
        event.target.consume(event)
    }
}