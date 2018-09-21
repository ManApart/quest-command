package travel

import core.events.EventListener
import core.gameState.GameState

class Arrive : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        if (event.origin != event.destination) {
            GameState.player.creature.location = event.destination
            println("You ${event.method} to ${event.destination}")
        }
    }

}