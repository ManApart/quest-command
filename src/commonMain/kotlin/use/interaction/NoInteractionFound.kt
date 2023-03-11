package use.interaction

import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe

class NoInteractionFound : EventListener<InteractEvent>() {

    override suspend fun shouldExecute(event: InteractEvent): Boolean {
        return EventManager.getNumberOfMatchingListeners(event, this) == 0
    }

    override suspend fun complete(event: InteractEvent) {
        if (event.creature.canInteract()) {
            event.creature.displayToMe("You don't seem to be able to do anything interesting with ${event.interactionTarget.name}.")
        } else {
            event.creature.displayToMe("You can't interact with ${event.interactionTarget.name} right now.")
        }
    }
}