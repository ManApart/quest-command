package core.ai.behavior

import core.events.Event
import core.events.EventManager
import quests.TriggeredEvent2

class Behavior<E : Event>(
        val name: String,
        private val triggeredEvent: TriggeredEvent2<E>
) {
    fun matches(event: Event): Boolean {
        return triggeredEvent.matches(event)
    }

    fun execute(event: Event) {
        triggeredEvent.execute(event)
    }

    fun copy(params: Map<String, String>) : Behavior<E> {
        return Behavior(name, triggeredEvent.copy(params = params))
    }

}