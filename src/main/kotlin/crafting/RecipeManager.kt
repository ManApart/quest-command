package crafting

import core.DependencyInjector
import core.thing.Thing

object RecipeManager {
    private var parser = DependencyInjector.getImplementation(RecipesCollection::class)
    private var recipes = parser.values.build()

    fun reset() {
        parser = DependencyInjector.getImplementation(RecipesCollection::class)
        recipes = parser.values.build()
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

    fun findCraftableRecipes(crafter: Thing, ingredients: List<Thing>, tool: Thing?): List<Recipe> {
        return recipes.filter { it.matches(crafter, ingredients, tool) && it.hasSkillsToCraft(crafter.soul) }
    }

    fun findRecipes(crafter: Thing, ingredients: List<Thing>, tool: Thing?): List<Recipe> {
        return recipes.filter { it.matches(crafter, ingredients, tool) }
    }
}