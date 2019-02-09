package travel.jump

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.AGILITY
import core.gameState.stat.HEALTH
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager
import travel.ArriveEvent

class PlayerFall : EventListener<FallEvent>() {
    override fun shouldExecute(event: FallEvent): Boolean {
        return event.creature == GameState.player.creature
    }

    override fun execute(event: FallEvent) {
        if (event.reason != null) display(event.reason)
        display("You fall ${event.fallHeight}ft.")
        takeDamage(event)
        if (GameState.player.creature.location != event.destination){
            EventManager.postEvent(ArriveEvent(destination = event.destination, method = "fall"))
        }
        GameState.player.finishJourney()
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