package crafting

import core.gameState.Soul
import core.utility.Named
import core.utility.listsMatch

data class Recipe(override val name: String, val ingredients: List<String>, val skills: Map<String, Int>, val tool: String, val result: String) : Named {

    fun matches(ingredients: List<String>, tool: String) : Boolean{
        return this.tool.toLowerCase() == tool.toLowerCase() && listsMatch(this.ingredients, ingredients)
    }

    fun canBeCookedBy(soul: Soul) : Boolean {
        skills.forEach{
            val current = soul.getStatOrNull(it.key)
            if (current == null || current.current < it.value) {
                return false
            }
        }
        return true
    }

    fun read() : String {
        //TODO - add required skills
        return "$name:" +
                "\n\tIngredients: ${ingredients.joinToString(", ")}" +
                "\n\tResult: $result"
    }
}