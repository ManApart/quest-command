package processing

import events.ArriveEvent
import events.EventListener
import events.TravelStartEvent
import gameState.GameState

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
            println("You leave ${event.currentLocation} travelling towards ${event.destination}")
            EventManager.postEvent(ArriveEvent(event.destination))
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