package status.status

import core.events.Event
import core.thing.Thing

class StatusEvent(val creature: Thing) : Event {

    override fun gameTicks(): Int {
        return 0
    }
}