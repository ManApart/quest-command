package crafting

import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import status.ExpGainedEvent
import status.stat.COOKING

class DiscoverRecipe : EventListener<DiscoverRecipeEvent>() {

    override suspend fun execute(event: DiscoverRecipeEvent) {
        if (event.source.thing.isPlayer()) {
            if (!event.source.mind.knows(event.recipe)) {
                event.source.mind.discover(event.recipe)

                val amount = event.recipe.skills.values.maxOrNull() ?: 1
                EventManager.postEvent(ExpGainedEvent(event.source.thing, COOKING, amount))
                event.source.displayToMe("You've discovered how to make ${event.recipe.name}!")
                event.source.displayToMe("\t${event.recipe.read()}")
            }
        }
    }



}