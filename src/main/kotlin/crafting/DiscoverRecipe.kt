package crafting

import core.events.EventListener
import core.events.EventManager
import core.history.display
import status.ExpGainedEvent
import status.stat.COOKING

class DiscoverRecipe : EventListener<DiscoverRecipeEvent>() {

    override fun execute(event: DiscoverRecipeEvent) {
        if (event.source.isPlayer()) {
            if (!event.source.knownRecipes.contains(event.recipe)) {
                event.source.knownRecipes.add(event.recipe)

                val amount = event.recipe.skills.values.maxOrNull() ?: 1
                EventManager.postEvent(ExpGainedEvent(event.source, COOKING, amount))
                display("You've discovered how to make ${event.recipe.name}!")
                display("\t${event.recipe.read()}")
            }
        }
    }



}