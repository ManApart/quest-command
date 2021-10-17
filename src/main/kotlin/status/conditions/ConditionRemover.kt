package status.conditions

import core.GameState
import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class ConditionRemover : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        GameState.player.thing.currentLocation().getAllSouls(GameState.player.thing).forEach { soul ->
            soul.getConditions().filter { !it.isStillViable() }.forEach { oldCondition ->
                EventManager.postEvent(RemoveConditionEvent(soul.parent, oldCondition))
            }
        }
    }
}