package status.statChanged

import core.GameState
import core.events.EventListener
import core.history.display
import status.stat.HEALTH
import status.stat.STAMINA

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target.isPlayer()
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> display("Oh dear, you have died!")
            STAMINA.lowercase() -> display("You are completely exhausted.")
        }
    }
}