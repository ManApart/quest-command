package traveling.jump

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import status.stat.AGILITY
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import kotlin.math.max

class PlayerFall : EventListener<FallEvent>() {
    override fun shouldExecute(event: FallEvent): Boolean {
        return event.creature == GameState.player
    }

    override fun execute(event: FallEvent) {
        if (event.reason != null) display(event.reason)
        display("You fall ${event.fallHeight}ft.")
        takeDamage(event)
        if (GameState.player.location != event.destination){
            EventManager.postEvent(ArriveEvent(destination = LocationPoint(event.destination), method = "fall"))
        }
        GameState.player.finishClimbing()
    }

    private fun takeDamage(event: FallEvent) {
        //TODO add defense per their foot defense
        val amount = max(0, event.fallHeight - event.creature.soul.getCurrent(AGILITY))
        if (amount != 0) {
            EventManager.postEvent(StatChangeEvent(event.creature, "Falling", HEALTH, -amount))
        } else {
            display("You land without taking damage.")
        }

    }
}