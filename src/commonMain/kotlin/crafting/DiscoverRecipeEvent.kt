package crafting

import core.Player
import core.events.Event

data class DiscoverRecipeEvent(val source: Player, val recipe: Recipe) : Event