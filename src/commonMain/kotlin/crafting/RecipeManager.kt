package crafting

import building.ModManager
import core.DependencyInjector
import core.GameState
import core.Player
import core.ai.AIManager
import core.startupLog
import core.thing.Thing
import core.utility.Backer
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList
import system.debug.DebugType

object RecipeManager {
    private val recipes = Backer(::loadRecipes)
    suspend fun getRecipes() = recipes.get()

    private suspend fun loadRecipes(): NameSearchableList<Recipe> {
        startupLog("Loading Recipes")
        return (DependencyInjector.getImplementation(RecipesCollection::class).values() + ModManager.recipes).build()
    }

    suspend fun reset() {
        recipes.reset()
    }

    suspend fun getRecipe(name: String): Recipe {
        return getRecipes().get(name)
    }

    suspend fun recipeExists(name: String): Boolean {
        return getRecipes().exists(name)
    }

    suspend fun getRecipeOrNull(name: String): Recipe? {
        return getRecipes().getOrNull(name)
    }

    suspend fun getAllRecipes(): NameSearchableList<Recipe> {
        return getRecipes().toNameSearchableList()
    }

    suspend fun getKnownRecipes(source: Player): NameSearchableList<Recipe> {
        return if (GameState.getDebugBoolean(DebugType.RECIPE_SHOW_ALL)) getAllRecipes() else {
            source.mind.memory.getListFact("Recipe")?.sources
                ?.mapNotNull { it.topic }?.let { getRecipes(it) }?.toNameSearchableList()
                ?: NameSearchableList()
        }
    }

    suspend fun getRecipes(names: List<String>): List<Recipe> {
        return names.map { getRecipes().getAll(it) }.flatten()
    }

    suspend fun findCraftableRecipes(crafter: Thing, ingredients: List<Thing>, tool: Thing?): List<Recipe> {
        return getRecipes().filter { it.matches(crafter, ingredients, tool) && it.hasSkillsToCraft(crafter.soul) }
    }

    suspend fun findRecipes(crafter: Thing, ingredients: List<Thing>, tool: Thing?): List<Recipe> {
        return getRecipes().filter { it.matches(crafter, ingredients, tool) }
    }
}