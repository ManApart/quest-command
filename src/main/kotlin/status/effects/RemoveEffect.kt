package status.effects

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul

class RemoveEffect: EventListener<RemoveEffectEvent>() {
    override fun shouldExecute(event: RemoveEffectEvent): Boolean {
        return event.target.hasSoul()
    }

    override fun execute(event: RemoveEffectEvent) {
        val soul = event.target.getSoul()!!
        soul.effects.remove(event.effect)
    }
}