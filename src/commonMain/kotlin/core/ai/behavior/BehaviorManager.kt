package core.ai.behavior

import building.ModManager
import core.DependencyInjector
import core.startupLog
import core.utility.lazyM


object BehaviorManager {
    private var behaviors by lazyM { loadBehaviors() }

    private fun loadBehaviors(): List<Behavior<*>> {
        return DependencyInjector.getImplementation(BehaviorsCollection::class).values + ModManager.behaviors
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

    fun getBehavior(recipe: BehaviorRecipe): Behavior<*> {
        return behaviors.firstOrNull { it.name == recipe.name }?.copy(recipe.params) ?: throw IllegalArgumentException("Could not find behavior for ${recipe.name}")
    }

}