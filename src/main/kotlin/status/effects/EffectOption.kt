package status.effects

import quests.triggerCondition.Condition
import quests.triggerCondition.ConditionalOption

data class EffectOption(override val option: EffectRecipe, override val condition: Condition = Condition())  : ConditionalOption<EffectRecipe> {
    override fun apply(params: Map<String, String>): EffectRecipe {
        return option
    }
}