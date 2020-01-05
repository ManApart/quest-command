package traveling.jump

import core.events.EventListener
import core.GameState
import traveling.location.location.LocationPoint
import status.stat.AGILITY
import status.stat.HEALTH
import core.history.display
import status.statChanged.StatChangeEvent
import core.events.EventManager
import traveling.arrive.ArriveEvent

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
        val amount = Math.max(0, event.fallHeight - event.creature.soul.getCurrent(AGILITY))
        if (amount != 0) {
            EventManager.postEvent(StatChangeEvent(event.creature, "Falling", HEALTH, -amount))
        } else {
            display("You land without taking damage.")
        }

    }
}