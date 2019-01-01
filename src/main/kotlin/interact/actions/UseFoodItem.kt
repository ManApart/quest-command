package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Item
import core.gameState.getCreature
import interact.eat.EatFoodEvent
import interact.UseEvent
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