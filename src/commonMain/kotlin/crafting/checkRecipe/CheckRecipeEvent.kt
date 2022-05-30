package crafting.checkRecipe

import core.Player
import core.events.Event
import crafting.Recipe

class CheckRecipeEvent(val source: Player, val recipe: Recipe? = null) : Event