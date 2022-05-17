package status.status

import core.Player
import core.events.Event
import core.thing.Thing

class StatusEvent(val source: Player, val creature: Thing) : Event {

    override fun gameTicks(): Int {
        return 0
    }
}