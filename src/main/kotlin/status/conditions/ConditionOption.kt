package status.conditions

import quests.triggerCondition.ConditionalOption

data class ConditionOption(override val option: ConditionRecipe, override val condition: (Map<String, String>) -> Boolean = { true })  : ConditionalOption<ConditionRecipe> {
    override fun apply(params: Map<String, String>): ConditionRecipe {
        return option
    }
}