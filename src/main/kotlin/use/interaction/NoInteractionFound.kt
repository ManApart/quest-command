package use.interaction

import core.events.Event
import core.events.EventListener
import core.GameState
import core.history.display
import core.events.EventManager

class NoInteractionFound : EventListener<InteractEvent>() {
    private var checkedEvent: Event? = null

    override fun shouldExecute(event: InteractEvent): Boolean {
        if (event != checkedEvent) {
            checkedEvent = event
            return EventManager.getNumberOfMatchingListeners(event) == 0
        }
        return false
    }

    override fun execute(event: InteractEvent) {
        if (GameState.player.canInteract) {
            display("You don't seem to be able to do anything interesting with ${event.target.name}.")
        } else {
            display("You can't interact with ${event.target.name} right now.")
        }
    }
}