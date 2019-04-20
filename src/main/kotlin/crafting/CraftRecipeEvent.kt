package crafting

import core.events.Event
import core.gameState.Target

class CraftRecipeEvent(val source: Target, val recipe: Recipe, val tool: Target? = null) : Event