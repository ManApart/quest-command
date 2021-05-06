package traveling.routes

import core.GameState
import core.events.EventListener
import core.history.display

class ViewRoute : EventListener<ViewRouteEvent>() {
    override fun execute(event: ViewRouteEvent) {
        when (val route = GameState.player.route) {
            null -> display("You currently don't have a route.")
            else -> display(route.getRouteProgressString(GameState.player.location))
        }
    }


}