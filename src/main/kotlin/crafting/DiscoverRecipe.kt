package crafting

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.Stat
import core.history.display
import status.ExpGainedEvent
import system.EventManager

class DiscoverRecipe : EventListener<DiscoverRecipeEvent>() {

    override fun execute(event: DiscoverRecipeEvent) {
        if (event.source == GameState.player.creature) {
            if (!GameState.player.knownRecipes.contains(event.recipe)) {
                GameState.player.knownRecipes.add(event.recipe)

                val amount = event.recipe.skills.values.max() ?: 1
                EventManager.postEvent(ExpGainedEvent(event.source, Stat.COOKING, amount))
                display("You've discovered how to make ${event.recipe.name}!")
                display("\t${event.recipe.read()}")
            }
        }
    }



}