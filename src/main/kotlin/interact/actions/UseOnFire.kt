package interact.actions

import core.events.EventListener
import core.history.display
import interact.UseEvent
import status.effects.AddConditionEvent
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class UseOnFire : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return event.target.soul.hasCondition("Burning")
    }

    override fun execute(event: UseEvent) {
        display("You place ${event.used.name} in the fire burning the ${event.target.name}.")
        val condition = Condition("Burning", Element.FIRE, 1, effects = listOf(EffectManager.getEffect("Burning", 1, 1)))
        EventManager.postEvent(AddConditionEvent(event.used, condition))
    }
}