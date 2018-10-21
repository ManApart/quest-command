package status.effects

import core.events.EventListener
import core.gameState.*
import interact.ScopeManager
import status.statChanged.StatChangeEvent
import system.gameTick.GameTickEvent

class RemoveEffect: EventListener<RemoveEffectEvent>() {
    override fun shouldExecute(event: RemoveEffectEvent): Boolean {
        return hasSoul(event.target)
    }

    override fun execute(event: RemoveEffectEvent) {
        val soul = getSoul(event.target)!!
        soul.effects.remove(event.effect)
    }
}