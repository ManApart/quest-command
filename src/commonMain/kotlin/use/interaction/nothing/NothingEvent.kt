package use.interaction.nothing

import core.events.TemporalEvent
import core.thing.Thing

class NothingEvent(override val source: Thing, private val hoursWaited: Int = 1, override var timeLeft: Int = 100) : TemporalEvent {
    override fun gameTicks(): Int = hoursWaited
}