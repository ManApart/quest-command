package dialogue

import core.utility.apply
import quests.triggerCondition.ConditionalOption

data class DialogueOption(override val option: String, override val condition: (Map<String, String>) -> Boolean = { true }) : ConditionalOption<String> {
    override fun apply(params: Map<String, String>): String {
        return option.apply(params)
    }
}