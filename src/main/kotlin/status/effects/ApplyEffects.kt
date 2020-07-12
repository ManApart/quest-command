package status.effects

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        //TODO - all scopes
        GameState.currentLocation().getAllSouls().forEach {
            (0 until event.time).forEach { _ ->
                it.applyConditions()
            }
        }
    }


}