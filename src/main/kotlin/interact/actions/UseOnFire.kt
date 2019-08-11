package interact.actions

import core.events.EventListener
import core.history.display
import interact.UseEvent
import status.effects.AddConditionEvent
import status.effects.EffectManager
import system.EventManager

class UseOnFire : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return event.used.soul.hasStat("burnHealth")
                && event.target.soul.hasCondition("Burning")
    }

    override fun execute(event: UseEvent) {
        display("You place ${event.used.name} in the fire burning the ${event.target.name}.")
        val item = event.used
        EventManager.postEvent(AddConditionEvent(item, EffectManager.getEffect("Burning")))
    }
}