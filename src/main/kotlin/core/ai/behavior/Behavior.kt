package core.ai.behavior

import core.events.Event
import core.events.EventManager

data class Behavior<E : Event>(
        val name: String,
        private val triggerEventClass: Class<E>,
        private val condition: (E, Map<String, String>) -> Boolean = { _, _ -> true },
        private val createEvents: (E, Map<String, String>) -> List<Event> = { _, _ -> listOf() },
        val params: Map<String, String> = mapOf()
) {
    fun matches(event: Event): Boolean {
        @Suppress("UNCHECKED_CAST")
        return event.javaClass == triggerEventClass && condition(event as E, params)
    }

    fun execute(event: Event) {
        createEvents(event as E, params).forEach { EventManager.postEvent(it) }
    }
}