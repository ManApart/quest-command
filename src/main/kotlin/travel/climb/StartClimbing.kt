package travel.climb

import core.events.EventListener
import core.gameState.Target
import core.gameState.GameState
import system.EventManager

class StartClimbing : EventListener<StartClimbingEvent>() {
    override fun shouldExecute(event: StartClimbingEvent): Boolean {
        return event.creature == GameState.player && event.target.properties.isActivator() && event.target.climb != null
    }

    override fun execute(event: StartClimbingEvent) {
        val climb = event.target.climb
        if (climb != null) {
            val path = ClimbPathManager.getPath(climb.name)
GameState.player.climbJourney = ClimbJourney(event.target, GameState.player.location, climb.destination, climb.upwards, path)
            EventManager.postEvent(ClimbJourneyEvent(path.getStart(climb.upwards), event.force))
        }
        GameState.player.canRest = false
        GameState.player.canTravel = false
        GameState.player.canInteract = false
        event.target.consume(event)
    }
}