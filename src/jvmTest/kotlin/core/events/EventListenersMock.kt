package core.events

class EventListenerMapMock(override val values: Map<String, List<EventListener<*>>> = mutableMapOf()) : EventListenerMapCollection {
    constructor(event: Event, listener: EventListener<*>): this(mapOf(event::class.qualifiedName!! to listOf(listener)))
    constructor(eventQualifiedName: String, listener: EventListener<*>): this(mapOf(eventQualifiedName to listOf(listener)))
}
fun listenerMock(values: Map<Event, EventListener<*>>) = EventListenerMapMock(values.entries.associate { (event, listener) -> event::class.qualifiedName!! to listOf(listener) })