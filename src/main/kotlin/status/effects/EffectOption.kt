package status.effects

import quests.triggerCondition.ConditionalOption

data class EffectOption(override val option: EffectRecipe, override val condition: (Map<String, String>) -> Boolean = { true })  : ConditionalOption<EffectRecipe> {
    override fun apply(params: Map<String, String>): EffectRecipe {
        return option
    }
}