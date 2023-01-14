package traveling.jump

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.withS
import explore.listen.addSoundEffect
import status.stat.AGILITY
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import kotlin.math.max

class PlayerFall : EventListener<FallEvent>() {
    override suspend fun shouldExecute(event: FallEvent): Boolean {
        return event.creature.isPlayer()
    }

    override suspend fun execute(event: FallEvent) {
        if (event.reason != null) event.creature.displayToMe(event.reason)
        event.creature.display{"${event.creature.asSubject(it)} ${event.creature.withS("fall", it)} ${event.fallHeight}ft."}
        takeDamage(event)
        event.creature.addSoundEffect("Falling", "a sharp thud", 20)
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
            event.creature.display{"${event.creature.asSubject(it)} land without taking damage."}
        }

    }
}