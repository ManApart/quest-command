package events

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
    }

    class ArrivalHandler(private val travelManager: TravelManager) : EventListener<ArriveEvent> {
        init {
            EventManager.registerListener(this)
        }
        override fun handle(event: ArriveEvent) {
            travelManager.logEvent()
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    fun logEvent(){

    }

}