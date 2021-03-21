package conversation.dialogue

import core.events.Event
import core.events.EventManager
import core.target.Target

class ConditionalDialogue(val condition: (Map<String, String>) -> Boolean, val events: List<(Target, Map<String, String>) -> Event> = listOf()) {
    fun matches(params: Map<String, String>): Boolean {
        return condition(params)
    }

    fun execute(parent: Target, params: Map<String, String>) {
        events.map { EventManager.postEvent(it(parent, params)) }
    }

}