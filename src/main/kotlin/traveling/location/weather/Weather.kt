package traveling.location.weather

import core.utility.Named
import status.effects.EffectRecipe

class Weather(override val name: String, val description: String, val effects: List<EffectRecipe> = listOf()) : Named {
}