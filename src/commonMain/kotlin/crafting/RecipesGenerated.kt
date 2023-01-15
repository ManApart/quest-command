package crafting

class RecipesGenerated : RecipesCollection {
    override suspend fun values() = listOf<RecipeResource>(resources.crafting.CommonRecipes()).flatMap { it.values() }
}