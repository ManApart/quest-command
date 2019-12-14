package core.ai.behavior

import core.DependencyInjector

object BehaviorManager {
    private var parser = DependencyInjector.getImplementation(BehaviorParser::class.java)
    private var behaviors = parser.loadBehaviors()

    fun reset() {
        parser = DependencyInjector.getImplementation(BehaviorParser::class.java)
        behaviors = parser.loadBehaviors()
    }

    fun behaviorExists(recipe: BehaviorRecipe): Boolean {
        return behaviors.firstOrNull { it.name.toLowerCase() == recipe.name.toLowerCase() } != null
    }

    fun getBehaviors(recipes: List<BehaviorRecipe>): List<Behavior> {
        return recipes.asSequence().map { getBehavior(it) }.filterNotNull().toList()
    }

    private fun getBehavior(recipe: BehaviorRecipe): Behavior? {
        val base = behaviors.firstOrNull { it.name.toLowerCase() == recipe.name.toLowerCase() }
        return if (base != null) {
            Behavior(base, recipe.params)
        } else {
            null
        }
    }


}