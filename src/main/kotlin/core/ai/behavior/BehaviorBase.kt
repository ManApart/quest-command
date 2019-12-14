package core.ai.behavior

import quests.triggerCondition.TriggerCondition
import quests.triggerCondition.TriggeredEvent

class BehaviorBase(val name: String, val condition: TriggerCondition, val events: List<TriggeredEvent> = listOf())
