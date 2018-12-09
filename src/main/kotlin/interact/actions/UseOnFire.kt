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
        return if (event.source is Item && event.source.soul.hasStat("burnHealth")){
            val soul = getSoul(event.target)
            soul != null && soul.hasEffect("On Fire")
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        display("You place ${event.source.name} in the fire burning the ${event.target.name}.")
        val item = (event.source as Item)
        EventManager.postEvent(AddEffectEvent(item, EffectManager.getEffect("Burning")))
        EventManager.postEvent(AddEffectEvent(item, EffectManager.getEffect("On Fire")))
    }
}