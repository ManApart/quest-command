package crafting

import status.Soul
import core.target.Target
import core.DependencyInjector

object RecipeManager {
    private var parser = DependencyInjector.getImplementation(RecipeParser::class.java)
    private var recipes = parser.loadRecipes()

    fun reset() {
        parser = DependencyInjector.getImplementation(RecipeParser::class.java)
        recipes = parser.loadRecipes()
    }

    fun getRecipe(name: String): Recipe {
        return recipes.get(name)
    }

    fun getRecipeOrNull(name: String): Recipe? {
        return recipes.getOrNull(name)
    }

    fun getRecipes(names: List<String>): List<Recipe> {
        return names.map { recipes.getAll(it) }.flatten()
    }

    fun findCraftableRecipes(ingredients: List<Target>, tool: Target?, soul: Soul): List<Recipe> {
        return recipes.filter { it.matches(ingredients, tool) && it.hasSkillsToCraft(soul) }
    }

    fun findRecipes(ingredients: List<Target>, tool: Target?): List<Recipe> {
        return recipes.filter { it.matches(ingredients, tool) }
    }
}