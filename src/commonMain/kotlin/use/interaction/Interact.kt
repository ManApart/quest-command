package use.interaction

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre

class Interact : EventListener<InteractEvent>() {

    override suspend fun complete(event: InteractEvent) {

        when {
            !event.interactionTarget.isWithinRangeOf(event.source) -> event.source.display { event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to interact with ${event.interactionTarget}." }
            !event.source.canInteract() -> event.source.displayToMe("You can't interact with ${event.interactionTarget.name} right now.")
            !event.interactionTarget.canConsume(event) -> event.source.displayToMe("${event.interactionTarget.name} doesn't seem to do anything interesting.")
            else  -> {
                event.interactionTarget.consume(event)
            }
        }
    }
}