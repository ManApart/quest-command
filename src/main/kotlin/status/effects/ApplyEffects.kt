package status.effects

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        //TODO - all scopes
        (0 until event.time).forEach { _ ->
            GameState.currentLocation().getAllSouls().forEach {
                it.applyConditions()
            }
        }
    }


}