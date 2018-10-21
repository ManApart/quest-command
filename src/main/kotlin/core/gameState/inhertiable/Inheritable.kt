package core.gameState.inhertiable

import com.fasterxml.jackson.annotation.JsonProperty
import core.gameState.Properties
import core.gameState.behavior.BehaviorRecipe

class Inheritable(val name: String, private val paramDefaults: Map<String, String> = mapOf(), @JsonProperty("behaviors") val behaviorRecipes: List<BehaviorRecipe> = listOf(), val properties: Properties = Properties()) {

    fun applyParams(inheritRecipe: InheritRecipe) : Inheritable {
        val params = paramDefaults.toMutableMap()
        inheritRecipe.params.entries.forEach {
            params[it.key] = it.value
        }

        val recipes = behaviorRecipes.map { it.copy(params) }
        return Inheritable(name, paramDefaults, recipes, properties.applyParams(params))
    }

}