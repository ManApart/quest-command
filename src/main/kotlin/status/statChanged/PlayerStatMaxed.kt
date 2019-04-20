package status.statChanged

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.HEALTH
import core.gameState.stat.STAMINA
import core.history.display

class PlayerStatMaxed : EventListener<StatMaxedEvent>() {
    override fun shouldExecute(event: StatMaxedEvent): Boolean {
        return event.target == GameState.player
    }

    override fun execute(event: StatMaxedEvent) {
        when (event.stat.toLowerCase()){
            HEALTH.toLowerCase() -> display("You feel the fullness of life beating in your bosom.")
            STAMINA.toLowerCase() -> display("You feel totally energized.")
        }
    }
}