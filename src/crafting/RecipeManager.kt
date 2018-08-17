package crafting

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator
import core.gameState.Item
import core.gameState.Soul

object RecipeManager {
    private val recipes = loadRecipes()

    private fun loadRecipes(): List<Recipe> {
        val json = this::class.java.getResourceAsStream("/resource/data/Recipes.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun getRecipe(name: String): Recipe {
        return recipes.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getRecipe(name: List<String>): Recipe {
        val fullName = name.joinToString(" ").toLowerCase()
        return recipes.first { fullName.contains(it.name.toLowerCase()) }
    }

    fun getRecipes(names: List<String>): List<Recipe> {
        return names.map { getRecipe(it) }.toList()
    }

    fun findRecipe(ingredients: List<Item>, tool: Activator, soul: Soul) : Recipe? {
        return findRecipes(ingredients, tool).firstOrNull { it.canBeCookedBy(soul)  }
    }

    fun findRecipes(ingredients: List<Item>, tool: Activator) : List<Recipe> {
        val toolName = tool.name
        val ingredientNames = ingredients.map { it.name }

        return recipes.filter { it.matches(ingredientNames, toolName) }
    }
}