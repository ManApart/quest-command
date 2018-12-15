package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.behavior.Behavior
import core.gameState.behavior.BehaviorBase
import core.gameState.behavior.BehaviorRecipe
import core.utility.JsonDirectoryParser

object BehaviorManager {
    private val behaviors = JsonDirectoryParser.parseDirectory("/data/generated/content/behaviors", ::parseFile)
    private fun parseFile(path: String): List<BehaviorBase> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

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