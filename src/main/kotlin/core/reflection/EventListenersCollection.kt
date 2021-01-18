package core.reflection

import core.events.EventListener

interface EventListenersCollection {
    val values: List<EventListener<*>>
}