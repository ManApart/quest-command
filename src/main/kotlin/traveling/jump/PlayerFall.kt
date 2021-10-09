package traveling.jump

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.asSubject
import status.stat.AGILITY
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import kotlin.math.max

class PlayerFall : EventListener<FallEvent>() {
    override fun shouldExecute(event: FallEvent): Boolean {
        return event.creature.isPlayer()
    }

    override fun execute(event: FallEvent) {
        if (event.reason != null) event.creature.display(event.reason)
        event.creature.display("${event.creature.asSubject()} fall ${event.fallHeight}ft.")
        takeDamage(event)
        if (event.creature.location != event.destination){
            EventManager.postEvent(ArriveEvent(event.creature, destination = LocationPoint(event.destination), method = "fall"))
        }
        event.creature.finishClimbing()
    }

    private fun takeDamage(event: FallEvent) {
        //TODO add defense per their foot defense
        val amount = max(0, event.fallHeight - event.creature.soul.getCurrent(AGILITY))
        if (amount != 0) {
            EventManager.postEvent(StatChangeEvent(event.creature, "Falling", HEALTH, -amount))
        } else {
            event.creature.display("${event.creature.asSubject()} land without taking damage.")
        }

    }
}