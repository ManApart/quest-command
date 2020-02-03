package dialogue

import core.utility.apply
import quests.triggerCondition.Condition
import quests.triggerCondition.ConditionalOption

data class DialogueOption(override val option: String, override val condition: Condition = Condition()) : ConditionalOption<String> {
    override fun apply(params: Map<String, String>): String {
        return option.apply(params)
    }
}