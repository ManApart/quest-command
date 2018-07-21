package travel.climb

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.consume
import system.EventManager

class StartClimbing : EventListener<ClimbStartEvent>() {
    override fun shouldExecute(event: ClimbStartEvent): Boolean {
        return event.creature == GameState.player.creature && event.target is Activator && event.target.climb != null
    }

    override fun execute(event: ClimbStartEvent) {
        val climb = (event.target as Activator).climb
        if (climb != null) {
            val path = ClimbPathManager.getPath(climb.name)
GameState.journey = ClimbJourney(event.target, GameState.player.creature.location, climb.destination, climb.upwards, path)
            EventManager.postEvent(ClimbJourneyEvent(path.getStart(climb.upwards), event.force))
        }
        event.target.consume(event)
    }
}