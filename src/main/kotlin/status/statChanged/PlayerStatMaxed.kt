package status.statChanged

import core.events.EventListener
import core.history.displayToMe
import status.stat.HEALTH
import status.stat.STAMINA

class PlayerStatMaxed : EventListener<StatMaxedEvent>() {
    override fun shouldExecute(event: StatMaxedEvent): Boolean {
        return event.target.isPlayer()
    }

    override fun execute(event: StatMaxedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> event.target.displayToMe("You feel the fullness of life beating in your bosom.")
            STAMINA.lowercase() -> event.target.displayToMe("You feel totally energized.")
        }
    }
}