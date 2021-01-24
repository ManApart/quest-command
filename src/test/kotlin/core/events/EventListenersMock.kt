package core.events
import core.events.EventListener

class EventListenersMock(override val values: List<EventListener<*>> = listOf()) : EventListenersCollection