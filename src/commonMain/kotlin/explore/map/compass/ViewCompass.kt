package explore.map.compass

import core.events.EventListener
import core.history.displayToMe
import explore.listen.addSoundEffect
import traveling.direction.Direction
import traveling.location.Route

class ViewCompass : EventListener<ViewCompassEvent>() {
    override suspend fun complete(event: ViewCompassEvent) {
        val route = updateRoute(event)
        if (route == null) {
            event.source.displayToMe("Could not find a valid destination to point to.")
        } else {
            val direction = route.getVectorDistance().direction
            if (direction == Direction.NONE) {
                event.source.displayToMe("${route.destination.name} is near you.")
            } else {
                event.source.displayToMe("${route.destination.name} is $direction of you.")
            }
            event.source.thing.addSoundEffect("Trinkets", "small clicks and whirring", 1)
        }
    }

    private fun updateRoute(event: ViewCompassEvent): Route? {
        val route = event.source.compassRoute
        route?.let { event.source.findRoute(route.destination, event.depth) }
        return event.source.compassRoute
    }

}