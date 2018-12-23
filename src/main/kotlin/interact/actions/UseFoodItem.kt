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
        return GameState.player.canInteract && event.source.properties.tags.has("Food") && event.source is Item && event.target.properties.tags.has("Creature")
    }

    override fun execute(event: UseEvent) {
        val creature = event.target.getCreature()!!
        EventManager.postEvent(EatFoodEvent(creature, event.source as Item))
    }

}