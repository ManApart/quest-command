package status.effects

import core.events.EventListener
import interact.ScopeManager
import system.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        ScopeManager.getAllSouls().forEach {
            it.applyEffects(event.time)
        }
    }


}