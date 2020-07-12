package status.conditions

import core.GameState
import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class ConditionRemover : EventListener<GameTickEvent>() {
    override fun execute(event: GameTickEvent) {
        GameState.currentLocation().getAllSouls().forEach { soul ->
            soul.getConditions().filter { !it.isStillViable() }.forEach { oldCondition ->
                EventManager.postEvent(RemoveConditionEvent(soul.parent, oldCondition))
            }
        }
    }
}