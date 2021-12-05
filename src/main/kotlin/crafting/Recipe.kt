package crafting

import core.properties.Properties
import core.thing.Thing
import core.utility.Named
import status.Soul

data class Recipe(
    override val name: String,
    val ingredients: Map<String, RecipeIngredient>,
    val skills: Map<String, Int> = mapOf(),
    val toolProperties: Properties = Properties(),
    val results: List<RecipeResult> = listOf(),
    val craftVerb: String = "craft"
) : Named {

    fun matches(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Boolean {
        return toolMatches(tool) && ingredientsMatch(crafter, ingredients, tool)
    }

    private fun toolMatches(tool: Thing?): Boolean {
        return (tool?.properties ?: Properties()).hasAll(this.toolProperties)
    }

    fun canBeCraftedBy(crafter: Thing, tool: Thing?): Boolean {
        return hasSkillsToCraft(crafter.soul) && matches(crafter, crafter.inventory.getAllItems(), tool)
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

    fun getUsedIngredients(crafter: Thing, availableItems: List<Thing>, tool: Thing?): Map<String, Pair<RecipeIngredient, Thing>> {
        val ingredientsLeft = availableItems.toMutableList()
        val usedIngredients = mutableMapOf<String, Pair<RecipeIngredient, Thing>>()
        this.ingredients.entries.forEach { (reference, recipeIngredient) ->
            val match = recipeIngredient.findMatchingIngredient(crafter, ingredientsLeft, tool)
            if (match != null) {
                ingredientsLeft.remove(match)
                usedIngredients[reference] = Pair(recipeIngredient, match)
            }
        }
        return usedIngredients
    }

    fun getMissingIngredients(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Map<String, RecipeIngredient> {
        val missing = mutableMapOf<String, RecipeIngredient>()
        val ingredientsLeft = ingredients.toMutableList()
        this.ingredients.entries.forEach { (reference, ingredient) ->
            val match = ingredient.findMatchingIngredient(crafter, ingredientsLeft, tool)
            if (match != null) {
                ingredientsLeft.remove(match)
            } else {
                missing[reference] = ingredient
            }
        }
        return missing
    }

    fun getResults(crafter: Thing, tool: Thing?, usedIngredients: Map<String, Pair<RecipeIngredient, Thing>>): List<Thing> {
        return results.map { it.getResult(crafter, tool, usedIngredients) }
    }

    private fun ingredientsMatch(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Boolean {
        val ingredientsLeft = ingredients.toMutableList()
        this.ingredients.values.forEach {
            val match = it.findMatchingIngredient(crafter, ingredientsLeft, tool)
            if (match != null) {
                ingredientsLeft.remove(match)
            } else if (!it.isOptional) {
                return false
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
            "\n\tIngredients: ${ingredients.values.joinToString(", ") { it.description }}"
        }
    }

    private fun readResults(): String {
        return if (results.isEmpty()) {
            ""
        } else {
            "\n\tResults: ${results.joinToString(", ") { it.description }}"
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
            "\n\tSkills: ${skills.entries.joinToString(", ") { "${it.value} ${it.key}" }}"
        }
    }


}