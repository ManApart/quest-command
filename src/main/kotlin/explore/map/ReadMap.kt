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
            println("You are in ${event.target.name}.")
        }
        val name = if (event.target.parent != null) {
            "${event.target.name} is a part of ${event.target.parent}. It"
        } else {
            event.target.name
        }
        display("$name ${getSiblings(event.target)}.")
    }

    private fun getSiblings(target: LocationNode): String {
        val locations = target.getNeighborLinks()
        return if (locations.isNotEmpty()) {
            val siblings = locations.joinToString(", ") { getLocationWithDirection(it, false) }
            "is neighbored by $siblings"
        } else {
            "has no known neighbors"
        }
    }

    private fun getLocationWithDirection(neighbor: LocationLink, far: Boolean): String {
        val direction = neighbor.position.getDirection()
        return if (direction == Direction.NONE) {
            neighbor.name
        } else {
            val farString = if (far) {
                "Far "
            } else ""
            "${neighbor.name} ($farString$direction)"
        }
    }

}