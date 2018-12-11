package crafting
import core.utility.NameSearchableList


class RecipeFakeParser(recipes: List<Recipe> = listOf()) : RecipeParser {
    val recipes = NameSearchableList(recipes)

    override fun loadRecipes(): NameSearchableList<Recipe> {
        return recipes
    }

}