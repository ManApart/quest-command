package combat.battle

import core.events.EventListener
import core.GameState
import core.events.EventManager
import time.gameTick.GameTickEvent

class BattleTick : EventListener<GameTickEvent>() {

    override fun shouldExecute(event: GameTickEvent): Boolean {
        return GameState.battle != null
    }

    override fun execute(event: GameTickEvent) {
        EventManager.postEvent(BattleTurnEvent())
    }
}