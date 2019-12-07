package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import crafting.DiscoverRecipeEvent

class DiscoverRecipeEventParser : EventParser {
    override fun className(): String {
        return DiscoverRecipeEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val recipeName = 0

        return DiscoverRecipeEvent(GameState.player, event.getRecipe(recipeName))
    }
}