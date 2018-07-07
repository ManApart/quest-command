package travel

import core.events.EventListener
import status.StatChangeEvent
import core.gameState.GameState
import core.gameState.Location
import core.gameState.Stat
import system.EventManager

object TravelManager {

    class TravelHandler : EventListener<TravelStartEvent>() {
        override fun execute(event: TravelStartEvent) {
            when {
                event.destination == event.currentLocation -> println("You realize that you're already at ${event.currentLocation}")
                isMovingToRestricted(event.currentLocation, event.destination) -> println("Could not find ${event.destination.name}")
                GameState.player.soul.getCurrent(Stat.StatType.STAMINA) == 0 -> println("You're too tired to do any traveling.")
                else -> {
                    if (event.currentLocation.contains(event.destination)){
                        println("You start travelling towards ${event.destination}")
                    } else {
                        println("You leave ${event.currentLocation} travelling towards ${event.destination}")
                    }
                    EventManager.postEvent(StatChangeEvent(GameState.player, "The journey", Stat.StatType.STAMINA, -1))
                    EventManager.postEvent(ArriveEvent(event.destination))
                }
            }
        }
    }

    private fun isMovingToRestricted(source: Location, destination: Location) : Boolean {
        val destinationRestrictionLocation = destination.getRestrictedParent()
        return !destinationRestrictionLocation.contains(source)
    }

    class ArrivalHandler() : EventListener<ArriveEvent>() {
        override fun execute(event: ArriveEvent) {
            GameState.player.location = event.destination
            println("You arrive at ${event.destination}")
        }
    }


}