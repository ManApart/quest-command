package crafting

import core.events.Event
import core.gameState.Creature

class CraftRecipeEvent(val source: Creature, val recipe: Recipe) : Event