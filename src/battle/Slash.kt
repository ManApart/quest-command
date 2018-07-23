package battle

import core.events.EventListener
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Location

class Slash : EventListener<SlashEvent>() {
    override fun execute(event: SlashEvent) {
       println("You slash ${event.target} with your ${event.sourcePart.equippedName()}.")
    }



}