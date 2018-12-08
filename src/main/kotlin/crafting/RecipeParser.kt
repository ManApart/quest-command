package crafting

import core.utility.NameSearchableList

interface RecipeParser {
    fun loadRecipes(): NameSearchableList<Recipe>
}