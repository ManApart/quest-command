package traveling.direction

import core.target.Target
import traveling.location.network.LocationNode

fun getDirection(player: Target, desiredDirection: Direction, target: Target, part: LocationNode): Direction {
    return if (desiredDirection != Direction.NONE) {
        desiredDirection
    } else {
        getDirection(player, target, part)
    }
}

fun getDirection(player: Target, target: Target, part: LocationNode): Direction {
    return if (part.network.getLocationRecipes().size > 1) {
        // or if we have multiple parts
        if (part.isAnOuterNode(Direction.ABOVE)) {
            //if we enter from the top part, we're going down
            Direction.BELOW
        } else {
            Direction.ABOVE
        }
    } else if (part.network.getLocationRecipes().size == 1) {
        //Or if it's a single part with a connection to another place, we're going in that direction
        val sourceConnection = target.location.getNeighborConnections().firstOrNull { it.source.location == player.location && it.source.targetName == target.name && it.source.partName == part.name }
        val destConnection = target.location.getNeighborConnections().firstOrNull { it.destination.location == player.location && it.source.targetName == target.name && it.source.partName == part.name }
        sourceConnection?.vector?.direction ?: destConnection?.vector?.invert()?.direction ?: Direction.NONE
    } else {
        Direction.NONE
    }
}
