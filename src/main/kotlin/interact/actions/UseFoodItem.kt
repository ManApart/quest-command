package interact.actions

import core.gameState.GameState
import interact.UseEvent
import interact.UseListener
import interact.eat.EatFoodEvent
import system.EventManager

class UseFoodItem : UseListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        //TODO - check event source, don't hardcode to player
        return GameState.player.canInteract
                && event.used.properties.tags.has("Food")
                && event.target.properties.tags.has("Creature")
    }

    override fun executeUseEvent(event: UseEvent) {
        EventManager.postEvent(EatFoodEvent(event.target, event.used))
    }

}