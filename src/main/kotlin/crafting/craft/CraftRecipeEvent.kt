package crafting.craft

import core.events.Event
import core.target.Target
import crafting.Recipe

class CraftRecipeEvent(val source: Target, val recipe: Recipe, val tool: Target? = null) : Event