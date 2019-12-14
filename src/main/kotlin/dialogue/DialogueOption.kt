package dialogue

import quests.triggerCondition.Condition

data class DialogueOption(val response: String, val condition: Condition = Condition())