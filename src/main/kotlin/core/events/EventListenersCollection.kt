package core.events

interface EventListenersCollection {
    val values: List<EventListener<*>>
}