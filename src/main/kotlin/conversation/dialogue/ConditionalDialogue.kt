package conversation.dialogue

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import core.target.Target
import quests.triggerCondition.Condition
import quests.triggerCondition.TriggeredEvent

@JsonIgnoreProperties(ignoreUnknown = true)
class ConditionalDialogue(val condition: Condition, val events: List<TriggeredEvent> = listOf()) {
    fun matches(params: Map<String, String>): Boolean {
        return condition.matches(params)
    }

    fun execute(parent: Target, params: Map<String, String>) {
        events.forEach { it.execute(parent, params) }
    }

}