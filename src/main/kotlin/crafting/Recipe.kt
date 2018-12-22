package crafting

import core.gameState.*
import core.gameState.Target
import core.utility.Named
import core.utility.listsMatch

data class Recipe(override val name: String, val ingredients: List<RecipeIngredient>, val skills: Map<String, Int> = mapOf(), val toolProperties: Properties = Properties(), val results: List<RecipeResult> = listOf()) : Named {

    fun matches(ingredients: List<Item>, tool: Target?): Boolean {
        return toolMatches(tool) && ingredientsMatch(ingredients)
    }

    private fun toolMatches(tool: Target?): Boolean {
        return (tool?.properties ?: Properties()).hasAll(this.toolProperties)
    }

    fun canBeCraftedBy(creature: Creature, tool: Target?) : Boolean {
        return hasSkillsToCraft(creature.soul) && matches(creature.inventory.getAllItems(), tool)
    }

    fun hasSkillsToCraft(soul: Soul): Boolean {
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

    fun getUsedIngredients(availableItems: List<Item>) : List<Item> {
        val ingredientsLeft = availableItems.toMutableList()
        val usedIngredients = mutableListOf<Item>()
        this.ingredients.forEach {
            val match = it.findMatchingIngredient(ingredientsLeft)
            if (match != null) {
                ingredientsLeft.remove(match)
                usedIngredients.add(match)
            }
        }
        return usedIngredients
    }

    fun getResults(usedIngredients: List<Item>) : List<Item> {
        return results.map { it.getResult(usedIngredients) }
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