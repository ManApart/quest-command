package crafting

import core.events.Event
import core.gameState.Creature
import core.gameState.Target

class DiscoverRecipeEvent(val source: Creature, val recipe: Recipe) : Event