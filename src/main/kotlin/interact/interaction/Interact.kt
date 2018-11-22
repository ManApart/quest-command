package interact.interaction

import core.events.Event
import core.events.EventListener
import core.gameState.GameState
import interact.UseEvent
import system.EventManager

class Interact : EventListener<InteractEvent>() {

    override fun execute(event: InteractEvent) {
        if (GameState.player.canInteract) {
//            println("You don't seem to be able to do anything interesting with ${event.target.name}.")
            event.target.consume(event)
        } else {
            println("You can't interact with ${event.target.name} right now.")
        }
    }
}