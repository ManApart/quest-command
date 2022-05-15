package core.ai.behavior

import core.DependencyInjector
import core.startupLog


object BehaviorManager {
    private var behaviors = loadBehaviors()

    private fun loadBehaviors(): List<Behavior<*>>{
        return DependencyInjector.getImplementation(BehaviorsCollection::class).values
    }

    fun reset() {
        startupLog("Loading Behaviors.")
        behaviors = loadBehaviors()
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