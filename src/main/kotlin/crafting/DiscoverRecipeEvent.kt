package crafting

import core.events.Event
import core.gameState.Target

class DiscoverRecipeEvent(val source: Target, val recipe: Recipe) : Event