package use.interaction

import core.events.EventListener
import core.history.display
import core.utility.StringFormatter

class Interact : EventListener<InteractEvent>() {

    override fun shouldExecute(event: InteractEvent): Boolean {
        return event.target.canConsume(event)
    }

    override fun execute(event: InteractEvent) {
        //TODO - should check if event.source can interact

        when {
            !event.target.isWithinRangeOf(event.source) -> display(StringFormatter.getSubject(event.source) + " " + StringFormatter.getIsAre(event.source) + " too far away to interact with ${event.target}.")
            event.source.canInteract() -> event.target.consume(event)
            else -> display("You can't interact with ${event.target.name} right now.")
        }
    }
}