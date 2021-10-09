package traveling.travel

import core.events.EventListener
import core.events.EventManager
import core.history.displayYou
import core.target.Target
import status.stat.STAMINA
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode

class TravelStart : EventListener<TravelStartEvent>() {
    override fun execute(event: TravelStartEvent) {
        when {
            event.destination == event.currentLocation -> event.creature.displayYou("You realize that you're already at ${event.currentLocation}")
            event.currentLocation.isMovingToRestricted(event.destination) -> event.creature.displayYou("You're not able to get to ${event.destination.name}")
            event.creature.soul.getCurrent(STAMINA) == 0 -> event.creature.displayYou("You're too tired to do any traveling.")
            !event.creature.isSafe() -> event.creature.displayYou("You can't travel right now.")
            event.creature.getEncumbrance() >= 1 -> event.creature.displayYou("You are too encumbered to travel.")
            else -> {
                if (!event.quiet) {
                    event.creature.displayYou("You leave ${event.currentLocation} travelling towards ${event.destination}.")
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
    EventManager.postEvent(ArriveEvent(source, destination = destination, method = "travel", quiet = quiet))
}