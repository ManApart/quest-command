package status.conditions

import magic.Element
import status.effects.EffectRecipe

class ConditionRecipeBuilder(
    private val name: String,
    private var element: Element = Element.NONE
) {
    private var elementStrength = 0
    private val effects = mutableListOf<EffectRecipe>()

    fun build(): ConditionRecipe {
        return ConditionRecipe(name, element, elementStrength, effects)
    }

    fun elementStrength(strength: Int) {
        this.elementStrength = strength
    }

    fun effect(vararg effect: EffectRecipe) {
        effects.addAll(effect)
    }

    fun effect(name: String, amount: Int, duration: Int = 1) {
        effects.add(EffectRecipe(name, amount, duration))
    }

    fun element(element: Element) {
        this.element = element
    }

    fun air() {
        this.element = Element.AIR
    }

    fun water() {
        this.element = Element.WATER
    }


}

fun condition(name: String, initializer: ConditionRecipeBuilder.() -> Unit): ConditionRecipe {
    return ConditionRecipeBuilder(name).apply(initializer).build()
}
