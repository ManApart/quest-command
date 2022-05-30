package core.events.multiEvent

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing

class StartMultiEvent(override val source: Thing, override var timeLeft: Int, val events: List<Event>) : Event, DelayedEvent {
    override fun getActionEvent(): MultiEvent {
        return MultiEvent(events)
    }
}