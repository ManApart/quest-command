package processing

import events.ArriveEvent
import events.EventListener
import events.StatChangeEvent
import events.TravelStartEvent
import gameState.GameState
import gameState.Location
import gameState.Stat

object TravelManager {

    class TravelHandler : EventListener<TravelStartEvent>() {
        override fun execute(event: TravelStartEvent) {
            when {
                event.destination == event.currentLocation -> println("You realize that you're already at ${event.currentLocation}")
                isMovingToRestricted(event.currentLocation, event.destination) -> println("You're unable to go directly to ${event.destination}")
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