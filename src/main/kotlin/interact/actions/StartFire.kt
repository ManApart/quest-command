package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.history.display
import interact.UseEvent
import status.effects.AddConditionEvent
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class StartFire : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return GameState.player.canInteract
                && event.used.properties.tags.has("Fire Starter")
                && event.target.properties.tags.has("Flammable")
    }

    override fun execute(event: UseEvent) {
        display("${event.target.name} catches on fire.")
        val condition = Condition("Burning", Element.FIRE, 1, listOf(EffectManager.getEffect("Burning", 1, 5)))
        EventManager.postEvent(AddConditionEvent(event.target, condition))
    }

}