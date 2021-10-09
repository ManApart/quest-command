package status.statChanged

import core.events.EventListener
import core.history.display
import core.history.displayYou
import status.stat.HEALTH
import status.stat.STAMINA

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target.isPlayer()
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> event.target.displayYou("Oh dear, you have died!")
            STAMINA.lowercase() -> event.target.displayYou("You are completely exhausted.")
        }
    }
}