package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.inhertiable.InheritRecipe
import core.gameState.inhertiable.Inheritable

object InheritableManager {
    private val inheritables = loadInheritables()

    private fun loadInheritables(): List<Inheritable> {
        val json = this::class.java.getResourceAsStream("/data/Inheritables.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun getInheritables(recipes: List<InheritRecipe>): List<Inheritable> {
        return recipes.map { getInheritable(it) }.toList()
    }

    fun getInheritable(recipe: InheritRecipe): Inheritable {
        return inheritables.first { it.name.toLowerCase() == recipe.name.toLowerCase() }.applyParams(recipe)
    }


}