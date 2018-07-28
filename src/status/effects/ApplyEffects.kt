package status.effects

import core.events.EventListener
import core.gameState.Activator
import core.gameState.Creature
import core.gameState.GameState
import interact.ScopeManager
import system.gameTick.GameTickEvent

class ApplyEffects: EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        GameState.player.creature.soul.applyEffects(event.time)
        ScopeManager.getTargets().forEach {
            if (it is Activator) {
                it.creature.soul.applyEffects(event.time)
            }else if (it is Creature){
                it.soul.applyEffects(event.time)
            }
        }
    }
}