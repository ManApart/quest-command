package explore.map.compass

import core.Player
import core.events.EventListener
import core.events.EventManager
import core.history.display
import traveling.location.RouteFinder
import traveling.location.location.LocationManager
import traveling.location.network.LocationNode

class SetCompassGoal : EventListener<SetCompassEvent>() {
    override fun execute(event: SetCompassEvent) {
        val sourceT = event.source.thing
        val destination = LocationManager.findLocationInAnyNetwork(sourceT, event.locationName)
        if (destination == null) {
            sourceT.display("Could not find ${event.locationName} on the map.")
        } else if (destination == sourceT.location) {
            event.source.findRoute(destination, event.depth)
            sourceT.display("You are at ${destination.name}.")
        } else {
            event.source.findRoute(destination, event.depth)
            if (event.source.compassRoute != null) {
                EventManager.postEvent(ViewCompassEvent(event.source))
            }
        }
    }
}

fun Player.findRoute(destination: LocationNode, depth: Int) {
    val existingRoute = compassRoute
    if (thing.location == existingRoute?.source && existingRoute.destination == destination) {
        compassRoute = existingRoute
    } else if (existingRoute != null && existingRoute.isOnRoute(thing.location)) {
        compassRoute = existingRoute.trim(thing.location)
    } else {
        val routeFinder = RouteFinder(thing.location, destination, depth, player = this)
        if (routeFinder.hasRoute()) {
            compassRoute = routeFinder.getRoute()
        } else {
            compassRoute = null
            display("Could not find a route to ${destination.name}.")
        }
    }
}