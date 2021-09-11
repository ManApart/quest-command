package traveling.climb

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.location.LocationPoint

class ClimbCompleteEvent(val creature: Target = GameState.player, val climbTarget: Target, val origin: LocationPoint, val destination: LocationPoint) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}