package status.rest

import core.events.Event
import core.target.Target

class RestEvent(val creature: Target, val hoursRested: Int) : Event {
    override fun gameTicks(): Int {
        return hoursRested
    }
}