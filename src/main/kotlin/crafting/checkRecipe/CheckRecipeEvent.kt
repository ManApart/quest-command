package crafting.checkRecipe

import core.events.Event
import core.target.Target
import crafting.Recipe

class CheckRecipeEvent(val source: Target, val recipe: Recipe? = null) : Event