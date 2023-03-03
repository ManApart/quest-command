package traveling.routes

import core.events.EventListener
import core.history.displayToMe

class ViewRoute : EventListener<ViewRouteEvent>() {
    override suspend fun complete(event: ViewRouteEvent) {
        when (val route = event.source.mind.route) {
            null -> event.source.displayToMe("You currently don't have a route.")
            else -> event.source.displayToMe(route.getRouteProgressString(event.source.location))
        }
    }


}