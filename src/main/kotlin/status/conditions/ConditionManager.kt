package status.conditions

import core.DependencyInjector
import core.utility.NameSearchableList
import status.effects.Effect
import status.effects.EffectManager
import status.effects.EffectRecipe
import traveling.location.location.LocationRecipe

object ConditionManager {
    private var conditionRecipes = loadConditions()

    fun reset() {
        conditionRecipes = loadConditions()
    }

    private fun loadConditions(): NameSearchableList<ConditionRecipe> {
        val parser = DependencyInjector.getImplementation(ConditionParser::class.java)
        return parser.loadConditions()
    }

    fun getCondition(name: String, bodyParts: List<LocationRecipe>): Condition {
        val recipe = conditionRecipes.get(name)
        return getCondition(recipe, bodyParts)
    }

    fun getCondition(recipe: ConditionRecipe, bodyParts: List<LocationRecipe>): Condition {
        val effects = recipe.effects.map { buildEffect(it, bodyParts) }
        val criticalEffects = recipe.criticalEffects.map { buildEffect(it, bodyParts) }
        return Condition(recipe.name, recipe.element, recipe.elementStrength, effects, criticalEffects, recipe.permanent)
    }

    private fun buildEffect(recipe: EffectRecipe, bodyParts: List<LocationRecipe>): Effect {
        return EffectManager.getEffect(recipe.name, recipe.amount, recipe.duration, bodyParts)
    }


}