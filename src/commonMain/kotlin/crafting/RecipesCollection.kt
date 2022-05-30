package crafting
import crafting.RecipeBuilder

interface RecipesCollection {
    val values: List<RecipeBuilder>
}