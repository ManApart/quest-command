package use.interaction

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre

class Interact : EventListener<InteractEvent>() {

    override suspend fun shouldExecute(event: InteractEvent): Boolean {
        return event.thing.canConsume(event)
    }

    override suspend fun execute(event: InteractEvent) {
        //TODO - should check if event.source can interact

        when {
            !event.thing.isWithinRangeOf(event.source) -> event.source.display{event.source.asSubject(it) + " " + event.source.isAre(it) + " too far away to interact with ${event.thing}."}
            event.source.canInteract() -> event.thing.consume(event)
            else -> event.source.displayToMe("You can't interact with ${event.thing.name} right now.")
        }
    }
}