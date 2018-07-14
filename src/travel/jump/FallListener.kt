package travel.jump

import core.events.EventListener
import system.EventManager
import travel.ArriveEvent

class FallListener : EventListener<FallEvent>() {
    override fun execute(event: FallEvent) {
        println("You fall down.")
        EventManager.postEvent(ArriveEvent(destination = event.destination))
    }
}