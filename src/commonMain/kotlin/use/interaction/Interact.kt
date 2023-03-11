package use.interaction

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre

class Interact : EventListener<InteractEvent>() {

    override suspend fun complete(event: InteractEvent) {

        when {
            !event.interactionTarget.isWithinRangeOf(event.creature) -> event.creature.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to interact with ${event.interactionTarget}." }
            !event.creature.canInteract() -> event.creature.displayToMe("You can't interact with ${event.interactionTarget.name} right now.")
            !event.interactionTarget.canConsume(event) -> event.creature.displayToMe("${event.interactionTarget.name} doesn't seem to do anything interesting.")
            else  -> {
                event.interactionTarget.consume(event)
            }
        }
    }
}