package travel

import core.events.EventListener
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.location.Route
import core.gameState.location.RouteFinder
import core.gameState.stat.Stat
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager

class FindRoute : EventListener<FindRouteEvent>() {
    override fun execute(event: FindRouteEvent) {
        val finder = RouteFinder(event.source, event.destination, event.depth)

        if (finder.hasRoute()){
            val route = finder.getRoute()
            GameState.player.route = route

            if (event.startImmediately){
                startTravel(route)
            } else {
                display(route.getRouteProgressString(GameState.player.creature.location))
            }

        } else {
            display("Unable to find a route.")
        }
    }

    private fun startTravel(route: Route){
        val source = GameState.player.creature.location
        when {
            route.destination == source -> display("You're already at the end of the route.")
            route.isOnRoute(source) -> EventManager.postEvent(TravelStartEvent(destination = route.getNextStep(source).destination))
        }
    }


}