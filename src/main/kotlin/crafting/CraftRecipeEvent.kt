package crafting

import core.events.Event
import core.gameState.Creature
import core.gameState.Target

class CraftRecipeEvent(val source: Creature, val recipe: Recipe, val tool: Target? = null) : Event