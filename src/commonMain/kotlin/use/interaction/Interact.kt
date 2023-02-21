package use.interaction

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre

class Interact : EventListener<InteractEvent>() {

    override suspend fun shouldExecute(event: InteractEvent): Boolean {
        return event.interactionTarget.canConsume(event)
    }

    override suspend fun execute(event: InteractEvent) {
        //TODO - should check if event.source can interact

        when {
            !event.interactionTarget.isWithinRangeOf(event.source) -> event.source.display{event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to interact with ${event.interactionTarget}."}
            event.source.canInteract() -> event.interactionTarget.consume(event)
            else -> event.source.displayToMe("You can't interact with ${event.interactionTarget.name} right now.")
        }
    }
}