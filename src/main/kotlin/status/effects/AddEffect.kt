package status.effects

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul

class AddEffect: EventListener<AddEffectEvent>() {
    override fun shouldExecute(event: AddEffectEvent): Boolean {
        return event.target.hasSoul()
    }
    override fun execute(event: AddEffectEvent) {
        val soul = event.target.getSoul()!!
        soul.effects.add(event.effect)
    }
}