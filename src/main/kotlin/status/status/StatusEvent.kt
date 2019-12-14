package status.status

import core.events.Event
import core.target.Target

class StatusEvent(val creature: Target) : Event {

    override fun gameTicks(): Int {
        return 0
    }
}