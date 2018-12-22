package crafting

import core.gameState.Item
import core.gameState.Soul
import core.gameState.Target
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

    fun findCraftableRecipes(ingredients: List<Item>, tool: Target?, soul: Soul) : List<Recipe> {
        return recipes.filter { it.matches(ingredients, tool) && it.hasSkillsToCraft(soul) }
    }

    fun findRecipes(ingredients: List<Item>, tool: Target?) : List<Recipe> {
        return recipes.filter { it.matches(ingredients, tool) }
    }
}