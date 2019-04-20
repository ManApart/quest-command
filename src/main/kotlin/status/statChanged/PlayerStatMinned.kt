package status.statChanged

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.HEALTH
import core.gameState.stat.STAMINA
import core.history.display

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target == GameState.player
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.toLowerCase()){
            HEALTH.toLowerCase() -> display("Oh dear, you have died!")
            STAMINA.toLowerCase() -> display("You are completely exhausted.")
        }
    }
}