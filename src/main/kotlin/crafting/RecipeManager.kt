package crafting

import core.DependencyInjector
import core.GameState
import core.Player
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import system.debug.DebugType

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

    fun recipeExists(name: String): Boolean{
        return recipes.exists(name)
    }

    fun getRecipeOrNull(name: String): Recipe? {
        return recipes.getOrNull(name)
    }

    fun getAllRecipes(): NameSearchableList<Recipe> {
        return recipes.toNameSearchableList()
    }

    fun getKnownRecipes(source: Player): NameSearchableList<Recipe> {
        return if (GameState.getDebugBoolean(DebugType.RECIPE_SHOW_ALL)) getAllRecipes() else source.knownRecipes
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