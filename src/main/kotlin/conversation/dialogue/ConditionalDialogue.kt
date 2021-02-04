package conversation.dialogue

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import core.events.Event
import core.events.EventManager
import core.target.Target

@JsonIgnoreProperties(ignoreUnknown = true)
class ConditionalDialogue(val condition: (Map<String, String>) -> Boolean, val events: List<(Target, Map<String, String>) -> Event> = listOf()) {
    fun matches(params: Map<String, String>): Boolean {
        return condition(params)
    }

    fun execute(parent: Target, params: Map<String, String>) {
        events.map { EventManager.postEvent(it(parent, params)) }
    }

}