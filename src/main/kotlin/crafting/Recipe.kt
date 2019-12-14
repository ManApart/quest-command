package crafting

import core.properties.Properties
import status.Soul
import core.target.Target
import core.utility.Named

data class Recipe(override val name: String, val ingredients: List<RecipeIngredient>, val skills: Map<String, Int> = mapOf(), val toolProperties: Properties = Properties(), val results: List<RecipeResult> = listOf(), val craftVerb: String = "craft") : Named {

    fun matches(ingredients: List<Target>, tool: Target?): Boolean {
        return toolMatches(tool) && ingredientsMatch(ingredients)
    }

    private fun toolMatches(tool: Target?): Boolean {
        return (tool?.properties ?: Properties()).hasAll(this.toolProperties)
    }

    fun canBeCraftedBy(creature: Target, tool: Target?): Boolean {
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

    fun getUsedIngredients(availableItems: List<Target>): List<Target> {
        val ingredientsLeft = availableItems.toMutableList()
        val usedIngredients = mutableListOf<Target>()
        this.ingredients.forEach {
            val match = it.findMatchingIngredient(ingredientsLeft)
            if (match != null) {
                ingredientsLeft.remove(match)
                usedIngredients.add(match)
            }
        }
        return usedIngredients
    }

    fun getResults(usedIngredients: List<Target>): List<Target> {
        return results.map { it.getResult(usedIngredients) }
    }

    private fun ingredientsMatch(ingredients: List<Target>): Boolean {
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