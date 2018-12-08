package explore.map

import core.events.EventListener
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
import core.history.display

class ReadMap : EventListener<ReadMapEvent>() {
    override fun execute(event: ReadMapEvent) {
        if (GameState.player.creature.location == event.target) {
            display("You are in ${event.target.name}.")
        }
        val name = if (event.target.parent != null) {
            "${event.target.name} is a part of ${event.target.parent}. It"
        } else {
            event.target.name
        }
        display("$name ${event.target.getSiblings()}.")
    }


}