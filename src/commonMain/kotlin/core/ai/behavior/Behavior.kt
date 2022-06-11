package core.ai.behavior

import core.events.Event
import quests.ConditionalEvents

data class Behavior<E : Event>(
        val name: String,
        private val triggeredEvent: ConditionalEvents<E>,
        val params: Map<String, String> = mapOf()
) {
    fun matches(event: Event): Boolean {
        return triggeredEvent.matches(event)
    }

    fun execute(event: Event) {
        triggeredEvent.execute(event)
    }

    fun copy(params: Map<String, String>) : Behavior<E> {
        return Behavior(name, triggeredEvent.copy(params = params), params)
    }

}