package crafting

import core.events.EventListener
import core.GameState

import status.stat.COOKING
import core.history.display
import status.ExpGainedEvent
import core.events.EventManager

class DiscoverRecipe : EventListener<DiscoverRecipeEvent>() {

    override fun execute(event: DiscoverRecipeEvent) {
        if (event.source.isPlayer()) {
            if (!GameState.player.knownRecipes.contains(event.recipe)) {
                GameState.player.knownRecipes.add(event.recipe)

                val amount = event.recipe.skills.values.maxOrNull() ?: 1
                EventManager.postEvent(ExpGainedEvent(event.source, COOKING, amount))
                display("You've discovered how to make ${event.recipe.name}!")
                display("\t${event.recipe.read()}")
            }
        }
    }



}