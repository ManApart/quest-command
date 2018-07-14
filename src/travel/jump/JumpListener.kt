package travel.jump

import core.events.EventListener
import system.EventManager
import travel.ArriveEvent

class JumpListener : EventListener<JumpEvent>() {
    override fun execute(event: JumpEvent) {
        println("You jump down.")
        EventManager.postEvent(ArriveEvent(destination = event.destination))
    }
}