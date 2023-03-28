package status.effects

import core.GameState
import core.events.EventListener
import core.events.EventManager
import status.conditions.RemoveConditionEvent
import time.gameTick.GameTickEvent

class ApplyEffects : EventListener<GameTickEvent>() {
    override suspend fun complete(event: GameTickEvent) {
        //TODO - all scopes
        GameState.players.values.forEach { player ->
            player.thing.currentLocation().getAllSouls(player.thing).forEach { soul ->
                (0 until event.time).forEach { _ ->
                    soul.applyConditions()
                }
            }
        }
    }


}