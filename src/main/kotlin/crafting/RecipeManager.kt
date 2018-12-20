package crafting

import core.gameState.Activator
import core.gameState.Item
import core.gameState.Soul
import system.DependencyInjector

object RecipeManager {
    private var parser = DependencyInjector.getImplementation(RecipeParser::class.java)
    private var recipes = parser.loadRecipes()

    fun reset() {
        parser = DependencyInjector.getImplementation(RecipeParser::class.java)
        recipes = parser.loadRecipes()
    }

    private fun getRecipe(name: String): Recipe {
        return recipes.get(name)
    }

    fun getRecipe(name: List<String>): Recipe {
        return recipes.get(name)
    }

    fun getRecipes(names: List<String>): List<Recipe> {
        return recipes.getAll(names)
    }

    fun findRecipe(ingredients: List<Item>, tool: Activator, soul: Soul) : Recipe? {
        return findRecipes(ingredients, tool).firstOrNull { it.canBeCookedBy(soul)  }
    }

    private fun findRecipes(ingredients: List<Item>, tool: Activator) : List<Recipe> {
        return recipes.filter { it.matches(ingredients, tool) }
    }
}