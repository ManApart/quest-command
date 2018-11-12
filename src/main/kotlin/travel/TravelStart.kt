package travel

import core.events.EventListener
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.stat.Stat
import status.statChanged.StatChangeEvent
import system.EventManager

class TravelStart : EventListener<TravelStartEvent>() {
    override fun execute(event: TravelStartEvent) {
        when {
            event.destination == event.currentLocation -> println("You realize that you're already at ${event.currentLocation}")
            isMovingToRestricted(event.currentLocation, event.destination) -> println("Could not find ${event.destination.name}")
            GameState.player.creature.soul.getCurrent(Stat.STAMINA) == 0 -> println("You're too tired to do any traveling.")
            !GameState.player.canTravel -> println("You can't travel right now.")
            else -> {
                println("You leave ${event.currentLocation} travelling towards ${event.destination}.")
                EventManager.postEvent(StatChangeEvent(GameState.player.creature, "The journey", Stat.STAMINA, -1))
                EventManager.postEvent(ArriveEvent(destination = event.destination, method = "travel"))
            }
        }
    }

    private fun isMovingToRestricted(source: LocationNode, destination: LocationNode): Boolean {
        //TODO - reimplement restricted?
        return false
    }
}