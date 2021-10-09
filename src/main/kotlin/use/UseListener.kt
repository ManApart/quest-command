package use

import core.events.EventListener
import core.history.display
import core.utility.isAre
import core.utility.asSubject

abstract class UseListener : EventListener<UseEvent>() {

    override fun execute(event: UseEvent) {
        if (!event.used.isWithinRangeOf(event.source)) {
            event.source.display(event.source.asSubject() + " " + event.source.isAre() + " too far away to use ${event.used}.")
        } else if (!event.target.isWithinRangeOf(event.source)) {
            event.source.display(event.source.asSubject() + " " + event.source.isAre() + " too far away to use ${event.used} on ${event.target}.")
        } else {
            executeUseEvent(event)
        }
    }

    abstract fun executeUseEvent(event: UseEvent)
}

