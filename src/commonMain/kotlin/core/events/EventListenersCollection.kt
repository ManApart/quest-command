package core.events
import core.events.EventListener

interface EventListenersCollection {
    val values: List<EventListener<*>>
}