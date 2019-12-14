package use.interaction

import core.events.Event
import core.GameState
import core.target.Target

class InteractEvent(val source: Target = GameState.player, val target: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}