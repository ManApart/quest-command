package status.conditions

import quests.triggerCondition.Condition
import quests.triggerCondition.ConditionalOption

data class ConditionOption(override val option: ConditionRecipe, override val condition: Condition = Condition())  : ConditionalOption<ConditionRecipe> {
    override fun apply(params: Map<String, String>): ConditionRecipe {
        return option
    }
}