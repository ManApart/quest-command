package crafting

import core.thing.Thing

//Crafter, Ingredient, Tool?
data class RecipeIngredient2(val matches: (Thing, Thing, Thing?) -> Boolean) {

    fun findMatchingIngredient(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Thing? {
        return ingredients.firstOrNull{ matches(crafter, it, tool)}
    }

    fun read() : String {
        return ""
    }

}