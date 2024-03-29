package use.actions

import core.events.EventManager
import core.history.display
import core.utility.asSubject
import magic.Element
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.effects.EffectManager
import use.UseEvent
import use.UseListener

class UseOnFire : UseListener() {

    override suspend fun shouldExecute(event: UseEvent): Boolean {
        return event.usedOn.soul.hasCondition("Burning")
    }

    override suspend fun executeUseEvent(event: UseEvent) {
        event.creature.display{"${event.creature.asSubject(it)} place ${event.used.name} in the fire burning the ${event.usedOn.name}."}
        val condition = Condition("Burning", Element.FIRE, 1, effects = listOf(EffectManager.getEffect("Burning", 1, 1)))
        EventManager.postEvent(AddConditionEvent(event.used, condition))
    }
}