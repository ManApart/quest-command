package status.conditions

import magic.Element

class ConditionRecipesBuilder(private val element: Element = Element.NONE) {
    private val conditionBuilders = mutableListOf<ConditionRecipeBuilder>()

    fun build(): List<ConditionRecipe>{
        return conditionBuilders.map { it.build() }
    }

    fun condition(name: String, initializer: ConditionRecipeBuilder.() -> Unit) {
        conditionBuilders.add(ConditionRecipeBuilder(name).apply(initializer))
    }

    fun conditions(element: Element = this.element, initializer: ConditionRecipesBuilder.() -> Unit) {
        conditionBuilders.addAll(ConditionRecipesBuilder(element).apply(initializer).conditionBuilders)
    }

}

fun conditions(initializer: ConditionRecipesBuilder.() -> Unit): List<ConditionRecipe> {
    return ConditionRecipesBuilder().apply(initializer).build()
}
