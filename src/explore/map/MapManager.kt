package explore.map

import core.events.EventListener
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Location

object MapManager {

    class MapHandler : EventListener<MapEvent>() {
        override fun execute(event: MapEvent) {
            if (GameState.player.location == event.target) {
                println("You are in ${event.target.name}, which is a part of ${event.target.getParent().name}.")
            }
            println("${event.target.name} ${getChildren(event.target)} and ${getSiblings(event.target)}.")
        }
    }

    private fun getChildren(target: Location): String {
        val locations = target.locations.filter { !it.restricted }
        return if (locations.isNotEmpty()) {
            val children = locations.joinToString(", ") { getLocationWithDirection(target, it, false) }
            "is made up of $children"
        } else {
            "doesn't have any known smaller locations"
        }
    }

    private fun getSiblings(target: Location): String {
        val locations = target.getParent().locations.filter { !it.restricted }
        return if (target.getParent() != target && locations.size > 1) {
            val siblings = locations.filter { it != target }.joinToString(", ") { getLocationWithDirection(target, it, true) }
            "is neighbored by $siblings"
        } else {
            "has no known neighbors"
        }
    }

    private fun getLocationWithDirection(target: Location, sibling: Location, far: Boolean): String {
        val direction = target.position.getDirection(sibling.position)
        return if (direction == Direction.NONE) {
            sibling.name
        } else {
            val farString = if (far) {"Far "} else ""
            "${sibling.name} ($farString$direction)"
        }
    }
}