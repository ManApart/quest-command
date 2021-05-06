package status.statChanged

import core.events.EventListener
import core.GameState
import status.stat.HEALTH
import status.stat.STAMINA
import core.history.display

class PlayerStatMaxed : EventListener<StatMaxedEvent>() {
    override fun shouldExecute(event: StatMaxedEvent): Boolean {
        return event.target == GameState.player
    }

    override fun execute(event: StatMaxedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> display("You feel the fullness of life beating in your bosom.")
            STAMINA.lowercase() -> display("You feel totally energized.")
        }
    }
}