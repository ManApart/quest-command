package travel.climb

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode

class ClimbCompleteEvent(val creature: Target = GameState.player, val target: Target, val origin: LocationNode, val destination: LocationNode) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}