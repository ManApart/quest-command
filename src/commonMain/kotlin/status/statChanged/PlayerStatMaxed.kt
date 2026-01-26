package status.statChanged

import core.events.EventListener
import core.history.displayToMe
import status.stat.Attributes.HEALTH
import status.stat.Attributes.STAMINA

class PlayerStatMaxed : EventListener<StatMaxedEvent>() {
    override suspend fun shouldExecute(event: StatMaxedEvent): Boolean {
        return event.thing.isPlayer()
    }

    override suspend fun complete(event: StatMaxedEvent) {
        when (event.stat.lowercase()){
            HEALTH.lowercase() -> event.thing.displayToMe("You feel the fullness of life beating in your bosom.")
            STAMINA.lowercase() -> event.thing.displayToMe("You feel totally energized.")
        }
    }
}