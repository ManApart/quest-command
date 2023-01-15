package crafting

interface RecipeResource {
    suspend fun values(): List<RecipeBuilder>
}