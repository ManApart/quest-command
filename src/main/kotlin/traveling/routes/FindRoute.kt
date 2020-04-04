package traveling.routes

import core.events.EventListener
import core.GameState
import traveling.location.Route
import traveling.location.RouteFinder
import core.history.display
import core.events.EventManager
import traveling.travel.TravelStartEvent

class FindRoute : EventListener<FindRouteEvent>() {
    override fun execute(event: FindRouteEvent) {
        if (event.source == event.destination){
            display("You are already there.")
            return
        }

        val finder = RouteFinder(event.source, event.destination, event.depth)

        if (finder.hasRoute()){
            val route = finder.getRoute()
            GameState.player.route = route

            if (event.startImmediately){
                startTravel(route, event.quiet)
            } else {
                display(route.getRouteProgressString(GameState.player.location))
            }

        } else {
            display("Unable to find a route.")
        }
    }

    private fun startTravel(route: Route, quiet: Boolean){
        val source = GameState.player.location
        when {
            route.destination == source -> display("You're already at the end of the route.")
            route.isOnRoute(source) -> EventManager.postEvent(TravelStartEvent(destination = route.getNextStep(source).destination.location, quiet = quiet))
        }
    }


}