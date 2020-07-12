package core.events.multiEvent

import core.events.DelayedEvent
import core.events.Event
import core.target.Target

class StartMultiEvent(override val source: Target, override var timeLeft: Int, val events: List<Event>) : Event, DelayedEvent {
    override fun getActionEvent(): MultiEvent {
        return MultiEvent(events)
    }
}