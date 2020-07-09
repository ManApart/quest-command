package use.actions

import core.events.EventManager
import core.history.display
import magic.Element
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.effects.EffectManager
import use.UseEvent
import use.UseListener

class StartFire : UseListener() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return event.source.canInteract()
                && event.used.properties.tags.has("Fire Starter")
                && event.target.properties.tags.has("Flammable")
    }

    override fun executeUseEvent(event: UseEvent) {
        display("${event.target.name} catches on fire.")
        val condition = Condition("Burning", Element.FIRE, 1, listOf(
                EffectManager.getEffect("Burning", 1, 5),
                EffectManager.getEffect("On Fire", 1, 5)
        ))
        EventManager.postEvent(AddConditionEvent(event.target, condition))
    }

}