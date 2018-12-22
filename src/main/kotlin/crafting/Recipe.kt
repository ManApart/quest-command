package crafting

import core.gameState.*
import core.gameState.Target
import core.utility.Named
import core.utility.listsMatch

data class Recipe(override val name: String, val ingredients: List<RecipeIngredient>, val skills: Map<String, Int> = mapOf(), val toolProperties: Properties = Properties(), val results: List<RecipeResult> = listOf(), val craftVerb: String = "craft") : Named {

    fun matches(ingredients: List<Item>, tool: Target?): Boolean {
        return toolMatches(tool) && ingredientsMatch(ingredients)
    }

    private fun toolMatches(tool: Target?): Boolean {
        return (tool?.properties ?: Properties()).hasAll(this.toolProperties)
    }

    fun canBeCraftedBy(creature: Creature, tool: Target?): Boolean {
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

    fun getUsedIngredients(availableItems: List<Item>): List<Item> {
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

    fun getResults(usedIngredients: List<Item>): List<Item> {
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

    fun read(): String {
        return "$name:" + readIngredients() + readSkills() + readTools() + readResults()
    }

    private fun readIngredients(): String {
        return if (ingredients.isEmpty()) {
            ""
        } else {
            "\n\tIngredients: ${ingredients.joinToString(", ") { it.read() }}"
        }
    }

    private fun readResults(): String {
        return if (results.isEmpty()) {
            ""
        } else {
            "\n\tResults: ${results.joinToString(", ") { it.read() }}"
        }
    }

    private fun readTools(): String {
        return if (toolProperties.isEmpty()) {
            ""
        } else {
            "\n\tTool: Something $toolProperties"
        }
    }

    private fun readSkills(): String {
        return if (skills.isEmpty()) {
            ""
        } else {
            "\n\tIngredients: ${skills.entries.joinToString(", ") { "${it.value} ${it.key}" }}"
        }
    }


}