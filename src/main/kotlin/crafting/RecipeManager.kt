package crafting

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator
import core.gameState.Item
import core.gameState.Soul
import core.utility.JsonDirectoryParser

object RecipeManager {
    private val recipes = JsonDirectoryParser.parseDirectory("/data/content/recipes", ::parseFile)
    private fun parseFile(path: String): List<Recipe> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    private fun getRecipe(name: String): Recipe {
        return recipes.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getRecipe(name: List<String>): Recipe {
        val fullName = name.joinToString(" ").toLowerCase()
        return recipes.first { fullName.contains(it.name.toLowerCase()) }
    }

    fun getRecipes(names: List<String>): List<Recipe> {
        return names.asSequence().map { getRecipe(it) }.toList()
    }

    fun findRecipe(ingredients: List<Item>, tool: Activator, soul: Soul) : Recipe? {
        return findRecipes(ingredients, tool).firstOrNull { it.canBeCookedBy(soul)  }
    }

    private fun findRecipes(ingredients: List<Item>, tool: Activator) : List<Recipe> {
        val toolName = tool.name
        val ingredientNames = ingredients.map { it.name }

        return recipes.filter { it.matches(ingredientNames, toolName) }
    }
}