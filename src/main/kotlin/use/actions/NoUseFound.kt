package use.actions

import core.events.Event
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.utility.isAre
import core.utility.asSubject
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
            if (!event.target.isWithinRangeOf(event.source)) {
                event.source.display{event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to interact with ${event.target}."}
            } else if (event.target.canConsume(event)) {
                event.target.consume(event)
            } else {
                event.source.display{"${event.source.asSubject(it)} use ${event.used.name} on ${event.target.name} but nothing happens."}
            }
        } else {
            event.source.displayToMe("You can't interact with that right now.")
        }
    }
}