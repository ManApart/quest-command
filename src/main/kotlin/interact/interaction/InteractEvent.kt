package interact.interaction

import core.events.Event
import core.gameState.Target

class InteractEvent(val target: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}