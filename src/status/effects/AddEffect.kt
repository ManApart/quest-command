package status.effects

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul

class AddEffect: EventListener<AddEffectEvent>() {
    override fun shouldExecute(event: AddEffectEvent): Boolean {
        return hasSoul(event.target)
    }
    override fun execute(event: AddEffectEvent) {
        val soul = getSoul(event.target)!!
        soul.effects.add(event.effect)
    }
}