package travel

import core.events.EventListener
import core.gameState.GameState
import core.history.display

class Arrive : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        if (event.origin != event.destination) {
            GameState.player.creature.location = event.destination
            display("You ${event.method} to ${event.destination}")
        }
    }

}