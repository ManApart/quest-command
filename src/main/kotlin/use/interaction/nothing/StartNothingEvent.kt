package use.interaction.nothing

import core.events.DelayedEvent
import core.events.Event
import core.target.Target

class StartNothingEvent(override val source: Target, private val hoursWaited: Int = 0) : Event, DelayedEvent {
    override var timeLeft = 100

    override fun getActionEvent(): Event {
        return NothingEvent(source, hoursWaited)
    }

}