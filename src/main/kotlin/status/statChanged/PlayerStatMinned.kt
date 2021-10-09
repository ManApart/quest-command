package status.statChanged

import core.events.EventListener
import core.history.displayToMe
import status.stat.HEALTH
import status.stat.STAMINA

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target.isPlayer()
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> event.target.displayToMe("Oh dear, you have died!")
            STAMINA.lowercase() -> event.target.displayToMe("You are completely exhausted.")
        }
    }
}