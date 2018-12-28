package interact.actions

import core.events.EventListener
import core.gameState.Item
import core.gameState.getSoul
import core.history.display
import interact.UseEvent
import status.effects.AddEffectEvent
import status.effects.EffectManager
import system.EventManager

class UseOnFire : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return event.used is Item
                && event.used.soul.hasStat("burnHealth")
                && event.target.getSoul()?.hasEffect("On Fire") ?: false
    }

    override fun execute(event: UseEvent) {
        display("You place ${event.used.name} in the fire burning the ${event.target.name}.")
        val item = (event.used as Item)
        EventManager.postEvent(AddEffectEvent(item, EffectManager.getEffect("Burning")))
        EventManager.postEvent(AddEffectEvent(item, EffectManager.getEffect("On Fire")))
    }
}