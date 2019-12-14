package status.effects

import core.events.EventListener
import traveling.scope.ScopeManager
import time.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        //TODO - all scopes
        (0 until event.time).forEach { _ ->
            ScopeManager.getScope().getAllSouls().forEach {
                it.applyConditions()
            }
        }
    }


}