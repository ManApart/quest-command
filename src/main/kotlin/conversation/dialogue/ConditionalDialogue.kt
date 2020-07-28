package conversation.dialogue

import quests.triggerCondition.TriggerCondition
import quests.triggerCondition.TriggeredEvent

class ConditionalDialogue(val condition: TriggerCondition, val events: List<TriggeredEvent> = listOf()) {
}