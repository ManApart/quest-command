package traveling.routes

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import core.target.Target
import system.debug.DebugType
import traveling.location.Route
import traveling.location.RouteFinder
import traveling.travel.TravelStartEvent

class FindRoute : EventListener<FindRouteEvent>() {
    override fun execute(event: FindRouteEvent) {
        if (event.sourceLocation == event.destination){
            event.source.displayToMe("You are already there.")
            return
        }

        val ignoreHidden = !GameState.getDebugBoolean(DebugType.MAP_SHOW_ALL_LOCATIONS)
        val finder = RouteFinder(event.sourceLocation, event.destination, event.depth, ignoreHidden, ignoreHidden)

        if (finder.hasRoute()){
            val route = finder.getRoute()
            event.source.route = route

            if (event.startImmediately){
                startTravel(event.source, route, event.quiet)
            } else {
                //TODO - make source instead of gamestate player
                event.source.displayToMe(route.getRouteProgressString(GameState.player.target.location))
            }

        } else {
            event.source.displayToMe("Unable to find a route.")
        }
    }

    private fun startTravel(source: Target, route: Route, quiet: Boolean){
        val sourceLocation = source.location
        when {
            route.destination == sourceLocation -> source.displayToMe("You're already at the end of the route.")
            route.isOnRoute(sourceLocation) -> EventManager.postEvent(TravelStartEvent(source, destination = route.getNextStep(sourceLocation).destination.location, quiet = quiet))
        }
    }


}