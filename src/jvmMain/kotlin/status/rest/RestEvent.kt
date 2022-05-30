package status.rest

import core.events.Event
import core.thing.Thing
import time.TimeManager

class RestEvent(val creature: Thing, val hoursRested: Int) : Event {
    override fun gameTicks(): Int {
        return hoursRested * TimeManager.ticksInHour
    }
}