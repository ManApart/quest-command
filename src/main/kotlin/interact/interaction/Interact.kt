package interact.interaction

import core.events.EventListener
import core.gameState.GameState
import core.history.display

class Interact : EventListener<InteractEvent>() {

    override fun shouldExecute(event: InteractEvent): Boolean {
        return event.target.canConsume(event)
    }

    override fun execute(event: InteractEvent) {
        if (GameState.player.canInteract) {
            event.target.consume(event)
        } else {
            display("You can't interact with ${event.target.name} right now.")
        }
    }
}