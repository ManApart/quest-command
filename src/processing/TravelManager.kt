package processing

import events.ArriveEvent
import events.EventListener
import events.StatChangeEvent
import events.TravelStartEvent
import gameState.GameState
import gameState.Soul
import gameState.Stat

class TravelManager {
    init {
        TravelHandler(this)
        ArrivalHandler(this)
    }

    class TravelHandler(private val travelManager: TravelManager) : EventListener<TravelStartEvent> {
        init {
            EventManager.registerListener(this)
        }

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

        override fun getPriority(): Int {
            return 0
        }
    }

    class ArrivalHandler(private val travelManager: TravelManager) : EventListener<ArriveEvent> {
        init {
            EventManager.registerListener(this)
        }

        override fun handle(event: ArriveEvent) {
            GameState.player.location = event.destination
            println("You arrive at ${event.destination}")
        }

        override fun getPriority(): Int {
            return 0
        }
    }


}