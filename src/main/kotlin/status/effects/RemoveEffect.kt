package status.effects

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul

class RemoveEffect: EventListener<RemoveEffectEvent>() {
    override fun shouldExecute(event: RemoveEffectEvent): Boolean {
        return hasSoul(event.target)
    }

    override fun execute(event: RemoveEffectEvent) {
        val soul = getSoul(event.target)!!
        soul.effects.remove(event.effect)
    }
}