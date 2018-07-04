package processing

import events.ArriveEvent
import events.EventListener
import events.StatChangeEvent
import events.TravelStartEvent
import gameState.GameState
import gameState.Stat

object TravelManager {

    class TravelHandler : EventListener<TravelStartEvent>() {

        override fun handle(event: TravelStartEvent) {
            when {
                event.destination == event.currentLocation -> println("You realize that you're already at ${event.currentLocation}")
                GameState.player.soul.getCurrent(Stat.StatType.STAMINA) == 0 -> println("You're too tired to do any traveling.")
                else -> {
                    println("You leave ${event.currentLocation} travelling towards ${event.destination}")
                    EventManager.postEvent(StatChangeEvent("The journey", Stat.StatType.STAMINA, -1))
                    EventManager.postEvent(ArriveEvent(event.destination))
                }
            }
        }
    }

    class ArrivalHandler() : EventListener<ArriveEvent>() {
        override fun handle(event: ArriveEvent) {
            GameState.player.location = event.destination
            println("You arrive at ${event.destination}")
        }
    }


}