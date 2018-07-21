package status.rest

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Stat
import status.rest.RestEvent
import status.StatChangeEvent
import system.EventManager

class PlayerRest : EventListener<RestEvent>() {
    override fun shouldExecute(event: RestEvent): Boolean {
        return true
    }

    override fun execute(event: RestEvent) {
        EventManager.postEvent(StatChangeEvent(GameState.player, "Resting", Stat.STAMINA, event.hoursRested))
    }
}