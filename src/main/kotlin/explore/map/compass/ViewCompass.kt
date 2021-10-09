package explore.map.compass

import core.events.EventListener
import core.history.display
import core.history.displayYou
import traveling.direction.Direction
import traveling.location.Route

class ViewCompass : EventListener<ViewCompassEvent>() {
    override fun execute(event: ViewCompassEvent) {
        val route = updateRoute(event)
        if (route == null) {
            event.source.displayYou("Could not find a valid destination to point to.")
        } else {
            val direction = route.getVectorDistance().direction
            if (direction == Direction.NONE) {
                event.source.displayYou("${route.destination.name} is near you.")
            } else {
                event.source.displayYou("${route.destination.name} is $direction of you.")
            }
        }
    }

    private fun updateRoute(event: ViewCompassEvent): Route? {
        val route = event.source.compassRoute
        route?.let { event.source.findRoute(route.destination, event.depth) }
        return event.source.compassRoute
    }

}