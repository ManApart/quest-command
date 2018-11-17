package status

import core.events.Event
import core.gameState.Creature

class StatusEvent(val creature: Creature) : Event {

    override fun gameTicks(): Int {
        return 0
    }
}