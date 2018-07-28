package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.behavior.Behavior
import core.gameState.behavior.BehaviorBase
import core.gameState.behavior.BehaviorRecipe

object BehaviorManager {
    private val behaviors = loadBehaviors()

    private fun loadBehaviors(): List<BehaviorBase> {
        val json = this::class.java.classLoader.getResource("core/data/behaviors.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun getBehaviors(recipes: List<BehaviorRecipe>): List<Behavior> {
        return recipes.map { getBehavior(it) }.toList()
    }

    fun getBehavior(recipe: BehaviorRecipe): Behavior {
        val base = behaviors.first { it.name.toLowerCase() == recipe.name.toLowerCase() }
        return Behavior(base, recipe.params)
    }


}