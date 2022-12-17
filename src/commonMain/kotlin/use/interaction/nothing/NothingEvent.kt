package use.interaction.nothing

import core.events.Event
import core.thing.Thing

class NothingEvent(val source: Thing, private val hoursWaited: Int = 1) : Event {
    override fun gameTicks(): Int = hoursWaited
}