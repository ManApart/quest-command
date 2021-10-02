package traveling.routes

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import traveling.location.Route
import traveling.location.RouteFinder
import traveling.travel.TravelStartEvent

class FindRoute : EventListener<FindRouteEvent>() {
    override fun execute(event: FindRouteEvent) {
        if (event.sourceLocation == event.destination){
            display("You are already there.")
            return
        }

        val finder = RouteFinder(event.sourceLocation, event.destination, event.depth)

        if (finder.hasRoute()){
            val route = finder.getRoute()
            event.source.route = route

            if (event.startImmediately){
                startTravel(event.source, route, event.quiet)
            } else {
                display(route.getRouteProgressString(GameState.player.location))
            }

        } else {
            display("Unable to find a route.")
        }
    }

    private fun startTravel(source: Target, route: Route, quiet: Boolean){
        val sourceLocation = source.location
        when {
            route.destination == sourceLocation -> display("You're already at the end of the route.")
            route.isOnRoute(sourceLocation) -> EventManager.postEvent(TravelStartEvent(source, destination = route.getNextStep(sourceLocation).destination.location, quiet = quiet))
        }
    }


}