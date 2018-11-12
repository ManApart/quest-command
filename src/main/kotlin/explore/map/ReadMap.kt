package explore.map

import com.sun.javafx.robot.impl.FXRobotHelper.getChildren
import core.events.EventListener
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode

class ReadMap : EventListener<ReadMapEvent>() {
    override fun execute(event: ReadMapEvent) {
        if (GameState.player.creature.location == event.target) {
            if (event.target.parent != null) {
                println("You are in ${event.target.name}, which is a part of ${event.target.parent}.")
            } else {
                println("You are in ${event.target.name}.")
            }
        }
        println("${event.target.name} ${getSiblings(event.target)}.")
    }

    private fun getSiblings(target: LocationNode): String {
        val locations = target.getNeighborLinks()
        return if (locations.size > 1) {
            //TODO - why always passing far?
            val siblings = locations.joinToString(", ") { getLocationWithDirection(it, true) }
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