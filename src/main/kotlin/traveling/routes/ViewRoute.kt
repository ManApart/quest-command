package traveling.routes

import core.events.EventListener
import core.history.display
import core.history.displayYou

class ViewRoute : EventListener<ViewRouteEvent>() {
    override fun execute(event: ViewRouteEvent) {
        when (val route = event.source.route) {
            null -> event.source.displayYou("You currently don't have a route.")
            else -> event.source.displayYou(route.getRouteProgressString(event.source.location))
        }
    }


}