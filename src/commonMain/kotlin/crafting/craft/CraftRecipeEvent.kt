package crafting.craft

import core.Player
import core.events.Event
import core.thing.Thing
import crafting.Recipe

class CraftRecipeEvent(val source: Player, val recipe: Recipe, val tool: Thing? = null) : Event