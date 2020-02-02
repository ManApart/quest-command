package dialogue

import quests.triggerCondition.Condition

data class DialogueOption(val choice: String, val condition: Condition = Condition())