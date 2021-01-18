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
        return behaviors.firstOrNull { it.name.equals(recipe.name, ignoreCase = true) } != null
    }

    fun getBehaviors(recipes: List<BehaviorRecipe>): List<Behavior<*>> {
        val recipeNames = recipes.map { it.name }
        return behaviors.filter { recipeNames.contains(it.name) }
    }

    fun getBehavior(recipe: BehaviorRecipe) : Behavior<*>{
        return behaviors.first { it.name == recipe.name }.copy(params = recipe.params)
    }

}