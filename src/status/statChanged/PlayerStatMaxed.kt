package status.statChanged

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.Stat

class PlayerStatMaxed : EventListener<StatMaxedEvent>() {
    override fun shouldExecute(event: StatMaxedEvent): Boolean {
        return event.creature == GameState.player.creature
    }

    override fun execute(event: StatMaxedEvent) {
        when (event.stat.toLowerCase()){
            Stat.HEALTH.toLowerCase() -> println("You feel the fullness of life beating in your bosom.")
            Stat.STAMINA.toLowerCase() -> println("You feel totally energized.")
        }
    }
}