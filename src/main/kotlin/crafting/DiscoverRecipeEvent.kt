package crafting

import core.events.Event
import core.target.Target

class DiscoverRecipeEvent(val source: Target, val recipe: Recipe) : Event