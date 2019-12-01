package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.history.display
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import interact.UseEvent
import interact.eat.EatFoodEvent
import system.EventManager

class UseFoodItem : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        //TODO - check event source, don't hardcode to player
        return GameState.player.canInteract
                && event.used.properties.tags.has("Food")
                && event.target.properties.tags.has("Creature")
    }

    override fun execute(event: UseEvent) {
        if (event.used.isWithinRangeOf(event.source)) {
            EventManager.postEvent(EatFoodEvent(event.target, event.used))
        } else {
            display(getSubject(event.source) + " " + getIsAre(event.source) + " too far away to eat ${event.used}")
        }
    }

}