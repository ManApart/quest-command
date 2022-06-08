package use.actions

import core.events.EventManager
import use.UseEvent
import use.UseListener
import use.eat.EatFoodEvent

class UseFoodItem : UseListener() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.source.canInteract()
                && event.used.properties.tags.has("Food")
                && event.usedOn.properties.tags.has("Creature")
    }

    override fun executeUseEvent(event: UseEvent) {
        EventManager.postEvent(EatFoodEvent(event.usedOn, event.used))
    }

}