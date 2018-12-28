package status.effects

import core.events.EventListener
import interact.scope.ScopeManager
import system.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        //TODO - all scopes
        ScopeManager.getScope().getAllSouls().forEach {
            it.applyEffects(event.time)
        }
    }


}