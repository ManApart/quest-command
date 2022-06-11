package use.interaction

import core.events.Event
import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe

class NoInteractionFound : EventListener<InteractEvent>() {
    private var checkedEvent: Event? = null

    override fun shouldExecute(event: InteractEvent): Boolean {
        if (event != checkedEvent) {
            checkedEvent = event
            return EventManager.getNumberOfMatchingListeners(event) == 0
        }
        return false
    }

    override fun execute(event: InteractEvent) {
        if (event.source.canInteract()) {
            event.source.displayToMe("You don't seem to be able to do anything interesting with ${event.thing.name}.")
        } else {
            event.source.displayToMe("You can't interact with ${event.thing.name} right now.")
        }
    }
}