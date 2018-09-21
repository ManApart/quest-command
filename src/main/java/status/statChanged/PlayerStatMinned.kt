package status.statChanged

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.Stat

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target == GameState.player.creature
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.toLowerCase()){
            Stat.HEALTH.toLowerCase() -> println("Oh dear, you have died!")
            Stat.STAMINA.toLowerCase() -> println("You are completely exhausted.")
        }
    }
}