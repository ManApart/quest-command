package status.effects

import core.events.EventListener
import core.gameState.Activator
import core.gameState.Creature
import core.gameState.GameState
import interact.ScopeManager
import system.gameTick.GameTickEvent

class AddEffect: EventListener<AddEffectEvent>() {
    override fun execute(event: AddEffectEvent) {
        event.creature.soul.effects.add(event.effect)
    }
}