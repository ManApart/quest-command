package interact.interaction

import core.events.EventListener
import core.gameState.GameState

class Interact : EventListener<InteractEvent>() {

    override fun shouldExecute(event: InteractEvent): Boolean {
        return event.target.canConsume(event)
    }

    override fun execute(event: InteractEvent) {
        if (GameState.player.canInteract) {
            event.target.consume(event)
        } else {
            println("You can't interact with ${event.target.name} right now.")
        }
    }
}