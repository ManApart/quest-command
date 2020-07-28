package conversation.dialogue

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggerCondition
import quests.triggerCondition.TriggeredEvent

@JsonIgnoreProperties(ignoreUnknown = true)
class ConditionalDialogue(val condition: TriggerCondition, val events: List<TriggeredEvent> = listOf()) {
    fun matches(event: Event): Boolean {
        return condition.matches(event)
    }

    fun execute(parent: Target, params: Map<String, String>) {
        events.forEach { it.execute(parent, params) }
    }

}