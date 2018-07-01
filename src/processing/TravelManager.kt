package processing

import events.ArriveEvent
import events.EventListener
import events.TravelStartEvent

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
            println("You arrive at $event.")
        }

        override fun getPriority(): Int {
            return 0
        }
    }


}