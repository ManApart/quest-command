package core.events

interface EventListenerMapCollection {
    val values: Map<String, List<EventListener<*>>>
}