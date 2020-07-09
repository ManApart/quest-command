package traveling.travel

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import status.stat.STAMINA
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint

class TravelStart : EventListener<TravelStartEvent>() {
    override fun execute(event: TravelStartEvent) {
        when {
            event.destination == event.currentLocation -> display("You realize that you're already at ${event.currentLocation}")
            event.currentLocation.isMovingToRestricted(event.destination) -> display("You're not able to get to ${event.destination.name}")
            GameState.player.soul.getCurrent(STAMINA) == 0 -> display("You're too tired to do any traveling.")
            !GameState.player.isSafe() -> display("You can't travel right now.")
            GameState.player.getEncumbrance() >= 1 -> display("You are too encumbered to travel.")
            else -> {
                if (!event.quiet) {
                    display("You leave ${event.currentLocation} travelling towards ${event.destination}.")
                }
                val distance = getDistanceToNeighbor(event.currentLocation, event.destination)
                postArriveEvent(event.creature, LocationPoint(event.destination), distance, event.quiet)
            }
        }
    }
}

fun getDistanceToNeighbor(source: LocationNode, destination: LocationNode): Int {
    return source.getConnection(destination)?.vector?.getDistance() ?: 1
}

fun postArriveEvent(source: Target, destination: LocationPoint, distance: Int, quiet: Boolean) {
    EventManager.postEvent(StatChangeEvent(source, "The journey", STAMINA, -distance / 10, silent = quiet))
    EventManager.postEvent(ArriveEvent(destination = destination, method = "travel", quiet = quiet))
}