package use

import core.events.EventListener
import core.history.display
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject

abstract class UseListener : EventListener<UseEvent>() {

    override fun execute(event: UseEvent) {
        if (!event.used.isWithinRangeOf(event.source)) {
            display(getSubject(event.source) + " " + getIsAre(event.source) + " too far away to use ${event.used}.")
        } else if (!event.target.isWithinRangeOf(event.source)) {
            display(getSubject(event.source) + " " + getIsAre(event.source) + " too far away to use ${event.used} on ${event.target}.")
        } else {
            executeUseEvent(event)
        }
    }

    abstract fun executeUseEvent(event: UseEvent)
}

