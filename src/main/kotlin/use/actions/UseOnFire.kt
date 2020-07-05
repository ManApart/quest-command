package use.actions

import core.history.display
import use.UseEvent
import use.UseListener
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class UseOnFire : UseListener() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return event.target.soul.hasCondition("Burning")
    }

    override fun executeUseEvent(event: UseEvent) {
        display("You place ${event.used.name} in the fire burning the ${event.target.name}.")
        val condition = Condition("Burning", Element.FIRE, 1, effects = listOf(EffectManager.getEffect("Burning", 1, 1)))
        EventManager.postEvent(AddConditionEvent(event.used, condition))
    }
}