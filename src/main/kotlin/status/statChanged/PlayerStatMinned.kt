package status.statChanged

import core.events.EventListener
import core.GameState
import status.stat.HEALTH
import status.stat.STAMINA
import core.history.display

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target == GameState.player
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> display("Oh dear, you have died!")
            STAMINA.lowercase() -> display("You are completely exhausted.")
        }
    }
}