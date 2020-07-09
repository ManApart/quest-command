package use.actions

import core.events.Event
import core.events.EventManager
import core.history.display
import core.utility.StringFormatter
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
                display(StringFormatter.getSubject(event.source) + " " + StringFormatter.getIsAre(event.source) + " too far away to interact with ${event.target}.")
            } else if (event.target.canConsume(event)) {
                event.target.consume(event)
            } else {
                display("You use ${event.used.name} on ${event.target.name} but nothing happens.")
            }
        } else {
            display("You can't interact with that right now.")
        }
    }
}