package conversation.dialogue

import core.events.Event
import core.events.EventManager
import core.thing.Thing

class ConditionalDialogue(val condition: (Map<String, String>) -> Boolean, val events: List<(Thing, Map<String, String>) -> Event> = listOf()) {
    fun matches(params: Map<String, String>): Boolean {
        return condition(params)
    }

    fun execute(parent: Thing, params: Map<String, String>) {
        events.map { EventManager.postEvent(it(parent, params)) }
    }

}