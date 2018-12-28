package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Item
import core.gameState.getCreature
import core.history.display
import core.utility.StringFormatter
import interact.EatFoodEvent
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class UseFoodItem : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        //TODO - check event source, don't hardcode to player
        return GameState.player.canInteract
                && event.used is Item
                && event.used.properties.tags.has("Food")
                && event.target.properties.tags.has("Creature")
    }

    override fun execute(event: UseEvent) {
        val creature = event.target.getCreature()!!
        EventManager.postEvent(EatFoodEvent(creature, event.used as Item))
    }

}