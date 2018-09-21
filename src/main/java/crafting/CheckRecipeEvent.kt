package crafting

import core.events.Event
import core.gameState.Target

class CheckRecipeEvent(val source: Target, val recipe: Recipe? = null) : Event