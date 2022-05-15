package status.conditions

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList
import status.effects.Effect
import status.effects.EffectManager
import status.effects.EffectRecipe
import traveling.location.location.Location

object ConditionManager {
    private var conditionRecipes by lazyM { loadConditions() }

    fun reset() {
        conditionRecipes = loadConditions()
    }

    private fun loadConditions(): NameSearchableList<ConditionRecipe> {
        startupLog("Loading Conditions.")
        val parser = DependencyInjector.getImplementation(ConditionsCollection::class)
        return parser.values.toNameSearchableList()
    }

    fun getCondition(name: String, bodyParts: List<Location>): Condition {
        val recipe = conditionRecipes.get(name)
        return getCondition(recipe, bodyParts)
    }

    fun getCondition(recipe: ConditionRecipe, bodyParts: List<Location>): Condition {
        val effects = recipe.effects.map { buildEffect(it, bodyParts) }
        val criticalEffects = recipe.criticalEffects.map { buildEffect(it, bodyParts) }
        return Condition(recipe.name, recipe.element, recipe.elementStrength, effects, criticalEffects, recipe.permanent)
    }

    private fun buildEffect(recipe: EffectRecipe, bodyParts: List<Location>): Effect {
        return EffectManager.getEffect(recipe.name, recipe.amount, recipe.duration, bodyParts)
    }


}