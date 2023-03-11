package status.rest

import core.events.TemporalEvent
import core.thing.Thing
import time.TimeManager

class RestEvent(override val creature: Thing, val hoursRested: Int, override var timeLeft: Int = hoursRested * TimeManager.ticksInHour) : TemporalEvent {
}