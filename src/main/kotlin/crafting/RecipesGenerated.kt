package crafting

class RecipesGenerated : RecipesCollection {
    override val values = listOf<RecipeResource>(resources.crafting.CommonRecipes()).flatMap { it.values }
}