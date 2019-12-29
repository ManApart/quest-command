package use.actions

import core.events.EventManager
import core.properties.CAN_INTERACT
import use.UseEvent
import use.UseListener
import use.eat.EatFoodEvent

class UseFoodItem : UseListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        //TODO - check event source, don't hardcode to player
        return event.source.properties.values.getBoolean(CAN_INTERACT)
                && event.used.properties.tags.has("Food")
                && event.target.properties.tags.has("Creature")
    }

    override fun executeUseEvent(event: UseEvent) {
        EventManager.postEvent(EatFoodEvent(event.target, event.used))
    }

}