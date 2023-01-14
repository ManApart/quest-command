package use

import core.events.EventListener
import core.history.display
import core.utility.asSubject
import core.utility.isAre

abstract class UseListener : EventListener<UseEvent>() {

    override suspend fun execute(event: UseEvent) {
        if (!event.used.isWithinRangeOf(event.source)) {
            event.source.display{event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to use ${event.used.name}."}
        } else if (!event.usedOn.isWithinRangeOf(event.source)) {
            event.source.display{event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to use ${event.used.name} on ${event.usedOn.name}."}
        } else {
            executeUseEvent(event)
        }
    }

    abstract suspend fun executeUseEvent(event: UseEvent)
}

