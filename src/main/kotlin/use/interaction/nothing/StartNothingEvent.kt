package use.interaction.nothing

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing

class StartNothingEvent(override val source: Thing, private val hoursWaited: Int = 0) : Event, DelayedEvent {
    override var timeLeft = 100

    override fun getActionEvent(): Event {
        return NothingEvent(source, hoursWaited)
    }

}