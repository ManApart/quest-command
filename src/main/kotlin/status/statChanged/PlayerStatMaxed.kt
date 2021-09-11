package status.statChanged

import core.GameState
import core.events.EventListener
import core.history.display
import status.stat.HEALTH
import status.stat.STAMINA

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