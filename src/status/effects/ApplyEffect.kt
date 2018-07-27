package status.effects

import core.events.EventListener
import core.gameState.Activator
import core.gameState.Creature
import core.gameState.GameState
import interact.ScopeManager
import system.gameTick.GameTickEvent

class ApplyEffect: EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        //TODO - apply effects to ALL souls
        GameState.player.creature.soul.applyEffects()
        ScopeManager.getTargets().forEach {
            if (it is Activator) {
                it.creature.soul.applyEffects()
            }else if (it is Creature){
                it.soul.applyEffects()
            }
        }
    }
}