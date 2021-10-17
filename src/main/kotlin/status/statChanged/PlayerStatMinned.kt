package status.statChanged

import core.events.EventListener
import core.history.displayToMe
import status.stat.HEALTH
import status.stat.STAMINA

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.thing.isPlayer()
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> event.thing.displayToMe("Oh dear, you have died!")
            STAMINA.lowercase() -> event.thing.displayToMe("You are completely exhausted.")
        }
    }
}