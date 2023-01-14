package use.actions

import core.events.EventManager
import core.history.display
import magic.Element
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.effects.EffectManager
import traveling.scope.LIT_LIGHT
import use.UseEvent
import use.UseListener

class StartFire : UseListener() {

    override suspend fun shouldExecute(event: UseEvent): Boolean {
        return event.source.canInteract()
                && event.used.properties.tags.has("Fire Starter")
                && event.usedOn.properties.tags.has("Flammable")
    }

    override suspend fun executeUseEvent(event: UseEvent) {
        event.source.display("${event.usedOn.name} catches on fire.")
        val litLevel = event.usedOn.properties.values.getInt(LIT_LIGHT, 1)
        val condition = Condition("Burning", Element.FIRE, 1, listOf(
                EffectManager.getEffect("Burning", 1, 5),
                EffectManager.getEffect("On Fire", 1, 5),
                EffectManager.getEffect("Lit", litLevel, 3)
        ))
        EventManager.postEvent(AddConditionEvent(event.usedOn, condition))
    }

}