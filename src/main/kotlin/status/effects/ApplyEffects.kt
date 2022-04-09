package status.effects

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        //TODO - all scopes
        GameState.players.values.forEach { player ->
            player.thing.currentLocation().getAllSouls(player.thing).forEach {
                (0 until event.time).forEach { _ ->
                    it.applyConditions()
                }
            }
        }
    }


}