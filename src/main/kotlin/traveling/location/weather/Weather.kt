package traveling.location.weather

import core.utility.Named
import quests.triggerCondition.Conditional
import status.effects.EffectOption
import status.effects.EffectRecipe

class Weather(override val name: String, val description: String, val effects: List<Conditional<EffectOption, EffectRecipe>> = listOf()) : Named {
}