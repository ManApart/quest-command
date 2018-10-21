package travel.climb

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location
import core.gameState.Target

class ClimbCompleteEvent(val creature: Creature = GameState.player.creature, val target: Target, val origin: Location, val destination: Location) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}