package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.behavior.Behavior
import core.gameState.behavior.BehaviorBase
import core.gameState.behavior.BehaviorRecipe

object BehaviorManager {
    private val behaviors = loadBehaviors()

    private fun loadBehaviors(): List<BehaviorBase> {
        val json = this::class.java.getResourceAsStream("/data/Behaviors.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun behaviorExists(recipe: BehaviorRecipe): Boolean {
        return behaviors.firstOrNull { it.name.toLowerCase() == recipe.name.toLowerCase() } != null
    }

    fun getBehaviors(recipes: List<BehaviorRecipe>): List<Behavior> {
        return recipes.asSequence().map { getBehavior(it) }.filterNotNull().toList()
    }

    private fun getBehavior(recipe: BehaviorRecipe): Behavior? {
        val base = behaviors.firstOrNull { it.name.toLowerCase() == recipe.name.toLowerCase() }
        return if (base != null) {
            Behavior(base, recipe.params)
        } else {
            null
        }
    }


}