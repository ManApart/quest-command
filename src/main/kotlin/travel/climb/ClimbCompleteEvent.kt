package travel.climb

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode

class ClimbCompleteEvent(val creature: Creature = GameState.player.creature, val target: Target, val origin: LocationNode, val destination: LocationNode) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}