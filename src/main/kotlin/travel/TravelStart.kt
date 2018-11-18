package travel

import core.events.EventListener
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.gameState.stat.Stat
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager

class TravelStart : EventListener<TravelStartEvent>() {
    override fun execute(event: TravelStartEvent) {
        when {
            event.destination == event.currentLocation -> display("You realize that you're already at ${event.currentLocation}")
            isMovingToRestricted(event.currentLocation, event.destination) -> display("You're not sure how to get to ${event.destination.name}")
            GameState.player.creature.soul.getCurrent(Stat.STAMINA) == 0 -> display("You're too tired to do any traveling.")
            !GameState.player.canTravel -> display("You can't travel right now.")
            else -> {
                display("You leave ${event.currentLocation} travelling towards ${event.destination}.")
                EventManager.postEvent(StatChangeEvent(GameState.player.creature, "The journey", Stat.STAMINA, -1))
                EventManager.postEvent(ArriveEvent(destination = event.destination, method = "travel"))
            }
        }
    }

    private fun isMovingToRestricted(source: LocationNode, destination: LocationNode): Boolean {
        val link = source.getLink(destination)
        return link == null || link.restricted
    }
}