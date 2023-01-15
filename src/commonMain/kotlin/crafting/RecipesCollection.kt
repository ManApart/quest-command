package crafting
import crafting.RecipeBuilder

interface RecipesCollection {
    suspend fun values(): List<RecipeBuilder>
}