package travel

import core.events.EventListener
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.stat.Stat
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager

class ViewRoute : EventListener<ViewRouteEvent>() {
    override fun execute(event: ViewRouteEvent) {
        val route = GameState.player.route
        when (route) {
            null -> display("You currently don't have a route.")
            else -> display(route.getRouteProgressString(GameState.player.creature.location))
        }
    }


}