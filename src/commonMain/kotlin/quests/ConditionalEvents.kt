package quests

import core.events.Event
import core.events.EventManager
import kotlin.reflect.KClass

data class ConditionalEvents<E: Event>(
    val triggerEvent: KClass<E>,
    private val condition: suspend (E, Map<String, String>) -> Boolean = { _, _ -> true },
    private val createEvents: suspend (E, Map<String, String>) -> List<Event> = { _, _ -> listOf() },
    val params: Map<String, String> = mapOf()
) {
    suspend fun matches(event: Event): Boolean {
        @Suppress("UNCHECKED_CAST")
        return event::class == triggerEvent && condition(event as E, params)
    }

    suspend fun execute(event: Event) {
        @Suppress("UNCHECKED_CAST")
        createEvents(event as E, params).forEach { EventManager.postEvent(it) }
    }
}