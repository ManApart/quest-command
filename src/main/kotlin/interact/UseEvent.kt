package interact

import core.events.Event
import core.gameState.Creature
import core.gameState.Target

class UseEvent(val source: Creature, val used: Target, val target: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}