package core.events.eventParsers

import core.events.Event
import core.GameState
import core.target.Target
import quests.triggerCondition.TriggeredEvent
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