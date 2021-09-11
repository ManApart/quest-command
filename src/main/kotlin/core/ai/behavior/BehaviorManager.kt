package core.ai.behavior

import core.DependencyInjector


object BehaviorManager {
    private var parser = DependencyInjector.getImplementation(BehaviorsCollection::class.java)
    private var behaviors = parser.values

    fun reset() {
        parser = DependencyInjector.getImplementation(BehaviorsCollection::class.java)
        behaviors = parser.values
    }

    fun behaviorExists(recipe: BehaviorRecipe): Boolean {
        return behaviors.firstOrNull { it.name.equals(recipe.name, ignoreCase = true) } != null
    }

    fun getBehaviors(recipes: List<BehaviorRecipe>): List<Behavior<*>> {
        val recipeNames = recipes.map { it.name }
        return behaviors.filter { recipeNames.contains(it.name) }
    }

    fun getBehavior(recipe: BehaviorRecipe) : Behavior<*>{
        return behaviors.firstOrNull { it.name == recipe.name }?.copy(recipe.params) ?: throw IllegalArgumentException("Could not find behavior for ${recipe.name}")
    }

}