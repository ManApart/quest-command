package use.actions

import core.events.Event
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre
import use.UseEvent
import use.UseListener

class NoUseFound : UseListener() {
    private var checkedEvent: Event? = null

    override fun shouldExecute(event: UseEvent): Boolean {
        if (event != checkedEvent) {
            checkedEvent = event
            return EventManager.getNumberOfMatchingListeners(event) == 0
        }
        return false
    }

    override fun executeUseEvent(event: UseEvent) {
        if (event.source.canInteract()) {
            if (!event.thing.isWithinRangeOf(event.source)) {
                event.source.display{event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to interact with ${event.thing}."}
            } else if (event.thing.canConsume(event)) {
                event.thing.consume(event)
            } else {
                event.source.display{"${event.source.asSubject(it)} use ${event.used.name} on ${event.thing.name} but nothing happens."}
            }
        } else {
            event.source.displayToMe("You can't interact with that right now.")
        }
    }
}