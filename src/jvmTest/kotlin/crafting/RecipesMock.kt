package crafting

class RecipesMock(val values: List<RecipeBuilder> = listOf()) : RecipesCollection{
    override suspend fun values() = values
}