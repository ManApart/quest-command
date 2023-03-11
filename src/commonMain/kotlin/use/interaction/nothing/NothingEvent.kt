package use.interaction.nothing

import core.events.TemporalEvent
import core.thing.Thing
import time.TimeManager

class NothingEvent(override val creature: Thing, private val hoursWaited: Int = 1, override var timeLeft: Int = hoursWaited * TimeManager.ticksInHour) : TemporalEvent