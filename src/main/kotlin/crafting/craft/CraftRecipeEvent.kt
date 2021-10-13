package crafting.craft

import core.Player
import core.events.Event
import core.target.Target
import crafting.Recipe

class CraftRecipeEvent(val source: Player, val recipe: Recipe, val tool: Target? = null) : Event