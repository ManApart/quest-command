package status.conditions

import core.GameState
import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class ConditionRemover : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        GameState.players.values.forEach { player ->
            player.thing.currentLocation().getAllSouls(player.thing).forEach { soul ->
                soul.getConditions().filter { !it.isStillViable() }.forEach { oldCondition ->
                    EventManager.postEvent(RemoveConditionEvent(soul.parent, oldCondition))
                }
            }
        }
    }
}