package use.interaction

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.utility.isAre
import core.utility.asSubject

class Interact : EventListener<InteractEvent>() {

    override fun shouldExecute(event: InteractEvent): Boolean {
        return event.target.canConsume(event)
    }

    override fun execute(event: InteractEvent) {
        //TODO - should check if event.source can interact

        when {
            !event.target.isWithinRangeOf(event.source) -> event.source.display(event.source.asSubject() + " " + event.source.isAre() + " too far away to interact with ${event.target}.")
            event.source.canInteract() -> event.target.consume(event)
            else -> event.source.displayToMe("You can't interact with ${event.target.name} right now.")
        }
    }
}