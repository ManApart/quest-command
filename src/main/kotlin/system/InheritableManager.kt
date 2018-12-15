package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.inhertiable.InheritRecipe
import core.gameState.inhertiable.Inheritable
import core.utility.JsonDirectoryParser

object InheritableManager {
    private val inheritables = JsonDirectoryParser.parseDirectory("/data/generated/content/inheritables", ::parseFile)
    private fun parseFile(path: String): List<Inheritable> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getInheritables(recipes: List<InheritRecipe>): List<Inheritable> {
        return recipes.asSequence().map { getInheritable(it) }.toList()
    }

    fun getInheritable(recipe: InheritRecipe): Inheritable {
        return inheritables.first { it.name.toLowerCase() == recipe.name.toLowerCase() }.applyParams(recipe)
    }


}