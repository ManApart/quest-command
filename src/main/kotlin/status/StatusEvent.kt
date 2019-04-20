package status

import core.events.Event
import core.gameState.Target

class StatusEvent(val creature: Target) : Event {

    override fun gameTicks(): Int {
        return 0
    }
}