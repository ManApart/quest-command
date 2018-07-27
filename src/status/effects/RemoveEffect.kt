package status.effects

import core.events.EventListener
import core.gameState.Activator
import core.gameState.Creature
import core.gameState.GameState
import interact.ScopeManager
import system.gameTick.GameTickEvent

class RemoveEffect: EventListener<RemoveEffectEvent>() {
    override fun execute(event: RemoveEffectEvent) {
        event.creature.soul.effects.remove(event.effect)
    }
}