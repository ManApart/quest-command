package traveling.travel

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import status.stat.STAMINA
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.LocationPoint

class TravelStart : EventListener<TravelStartEvent>() {
    override fun execute(event: TravelStartEvent) {
        when {
            event.destination == event.currentLocation -> display("You realize that you're already at ${event.currentLocation}")
            event.currentLocation.isMovingToRestricted(event.destination) -> display("You're not able to get to ${event.destination.name}")
            GameState.player.soul.getCurrent(STAMINA) == 0 -> display("You're too tired to do any traveling.")
            !GameState.player.canTravel -> display("You can't travel right now.")
            GameState.player.getEncumbrance() >= 1 -> display("You are too encumbered to travel.")
            else -> {
                if (!event.quiet) {
                    display("You leave ${event.currentLocation} travelling towards ${event.destination}.")
                }
                EventManager.postEvent(StatChangeEvent(GameState.player, "The journey", STAMINA, -1, silent = event.quiet))
                EventManager.postEvent(ArriveEvent(destination = LocationPoint(event.destination), method = "travel", quiet = event.quiet))
            }
        }
    }

}