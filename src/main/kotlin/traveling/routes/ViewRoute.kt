package traveling.routes

import core.events.EventListener
import core.GameState
import core.history.display

class ViewRoute : EventListener<ViewRouteEvent>() {
    override fun execute(event: ViewRouteEvent) {
        val route = GameState.player.route
        when (route) {
            null -> display("You currently don't have a route.")
            else -> display(route.getRouteProgressString(GameState.player.location))
        }
    }


}