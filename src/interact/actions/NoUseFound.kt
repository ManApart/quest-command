package interact.actions

import core.events.Event
import core.events.EventListener
import interact.UseEvent
import system.EventManager

class NoUseFound() : EventListener<UseEvent>() {
    var checkedEvent: Event? = null

    override fun shouldExecute(event: UseEvent): Boolean {
        if (event != checkedEvent){
            checkedEvent = event
            return EventManager.getNumberOfMatchingListeners(event) == 0
        }
        return false
    }

    override fun execute(event: UseEvent) {
        println("You use ${event.source.name} on ${event.target.name} but nothing happens.")
    }
}