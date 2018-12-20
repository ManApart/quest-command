package crafting

import core.gameState.Item
import core.gameState.Properties
import core.gameState.Soul
import core.gameState.Target
import core.utility.Named
import core.utility.listsMatch

data class Recipe(override val name: String, val ingredients: List<RecipeIngredient>, val skills: Map<String, Int> = mapOf(), val toolProperties: Properties = Properties(), val results: List<RecipeResult> = listOf()) : Named {

    fun matches(ingredients: List<Item>, tool: Target): Boolean {
        return tool.properties.hasAll(this.toolProperties) && ingredientsMatch(ingredients)
    }

    fun canBeCookedBy(soul: Soul): Boolean {
        skills.forEach {
            val current = soul.getStatOrNull(it.key)
            if (current == null || current.current < it.value) {
                return false
            }
        }
        return true
    }

    fun read(): String {
        //TODO - add required skills
        return "$name:" +
                "\n\tIngredients: ${ingredients.joinToString(", ")}"
//                "\n\tResult: $result"
    }

    private fun ingredientsMatch(ingredients: List<Item>): Boolean {
        val ingredientsLeft = ingredients.toMutableList()
        this.ingredients.forEach {
            val match = it.findMatchingIngredient(ingredientsLeft)
            if (match == null) {
                return false
            } else {
                ingredientsLeft.remove(match)
            }
        }
        return true
    }


}