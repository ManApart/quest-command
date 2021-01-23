package quests

import core.events.Event
import core.events.EventManager

data class ConditionalEvents<E: Event>(
        val triggerEvent: Class<E>,
        private val condition: (E, Map<String, String>) -> Boolean = { _, _ -> true },
        private val createEvents: (E, Map<String, String>) -> List<Event> = { _, _ -> listOf() },
        val params: Map<String, String> = mapOf()
) {
    fun matches(event: Event): Boolean {
        @Suppress("UNCHECKED_CAST")
        return event.javaClass == triggerEvent && condition(event as E, params)
    }

    fun execute(event: Event) {
        createEvents(event as E, params).forEach { EventManager.postEvent(it) }
    }
}