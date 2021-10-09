package explore.map.compass

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import traveling.location.RouteFinder
import traveling.location.location.LocationManager
import traveling.location.network.LocationNode

class SetCompassGoal : EventListener<SetCompassEvent>() {
    override fun execute(event: SetCompassEvent) {
        val destination = LocationManager.findLocationInAnyNetwork(event.source, event.locationName)
        if (destination == null) {
            display("Could not find ${event.locationName} on the map.")
        } else if (destination == event.source.location){
            event.source.findRoute(destination, event.depth)
            display("You are at ${destination.name}.")
        } else {
            event.source.findRoute(destination, event.depth)
            if (event.source.compassRoute != null) {
                EventManager.postEvent(ViewCompassEvent(event.source))
            }
        }
    }
}

fun Target.findRoute(destination: LocationNode, depth: Int) {
    val existingRoute = compassRoute
    if (location == existingRoute?.source && existingRoute.destination == destination) {
        compassRoute = existingRoute
    } else if (existingRoute != null && existingRoute.isOnRoute(location)) {
        compassRoute = existingRoute.trim(location)
    } else {
        val routeFinder = RouteFinder(location, destination, depth)
        if (routeFinder.hasRoute()) {
            compassRoute = routeFinder.getRoute()
        } else {
            compassRoute = null
            display("Could not find a route to ${destination.name}.")
        }
    }
}