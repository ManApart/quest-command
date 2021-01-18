package core.ai.behavior

import core.events.Event
import core.events.EventManager

class Behavior2<E : Event>(
        val name: String,
        private val triggerEventClass: Class<E>,
        private val condition: (E, Map<String, String>) -> Boolean = { _, _ -> true },
        private val createEvents: (E, Map<String, String>) -> List<Event> = { _, _ -> listOf() },
        private val params: Map<String, String> = mapOf()
) {
    fun matches(event: Event): Boolean {
        @Suppress("UNCHECKED_CAST")
        return event.javaClass == triggerEventClass.javaClass && condition(event as E, params)
    }

    fun execute(event: E) {
        createEvents(event, params).forEach { EventManager.postEvent(it) }
    }
}