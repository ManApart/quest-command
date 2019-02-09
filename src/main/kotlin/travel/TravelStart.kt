package travel

import core.events.EventListener
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.stat.STAMINA
import core.gameState.stat.Stat
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager

class TravelStart : EventListener<TravelStartEvent>() {
    override fun execute(event: TravelStartEvent) {
        when {
            event.destination == event.currentLocation -> display("You realize that you're already at ${event.currentLocation}")
            event.currentLocation.isMovingToRestricted(event.destination) -> display("You're not able to get to ${event.destination.name}")
            GameState.player.creature.soul.getCurrent(STAMINA) == 0 -> display("You're too tired to do any traveling.")
            !GameState.player.canTravel -> display("You can't travel right now.")
            else -> {
                display("You leave ${event.currentLocation} travelling towards ${event.destination}.")
                EventManager.postEvent(StatChangeEvent(GameState.player.creature, "The journey", STAMINA, -1))
                EventManager.postEvent(ArriveEvent(destination = event.destination, method = "travel"))
            }
        }
    }

}