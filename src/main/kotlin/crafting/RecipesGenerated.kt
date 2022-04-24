package crafting

class RecipesGenerated : RecipesCollection {
    override val values by lazy { listOf<RecipeResource>(resources.crafting.CommonRecipes()).flatMap { it.values }}
}