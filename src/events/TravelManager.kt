package events

class TravelManager {

    class TravelHandler(private val travelManager: TravelManager) : EventListener<TravelEvent> {
        init {
            EventManager.registerListener(this)
        }
        override fun handle(event: TravelEvent) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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