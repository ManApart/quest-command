package status.effects

import core.utility.Named

data class EffectRecipe(override val name: String, val amount: Int, val duration: Int) : Named