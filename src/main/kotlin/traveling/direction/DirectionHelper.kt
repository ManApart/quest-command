package traveling.direction

import core.thing.Thing
import traveling.location.network.LocationNode

fun getDirection(player: Thing, desiredDirection: Direction, thing: Thing, part: LocationNode): Direction {
    return if (desiredDirection != Direction.NONE) {
        desiredDirection
    } else {
        getDirection(player, thing, part)
    }
}

fun getDirection(player: Thing, thing: Thing, part: LocationNode): Direction {
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
        val sourceConnection = thing.location.getNeighborConnections().firstOrNull { it.source.location == player.location && it.source.thingName == thing.name && it.source.partName == part.name }
        val destConnection = thing.location.getNeighborConnections().firstOrNull { it.destination.location == player.location && it.source.thingName == thing.name && it.source.partName == part.name }
        sourceConnection?.originPoint?.direction ?: destConnection?.originPoint?.invert()?.direction ?: Direction.NONE
    } else {
        Direction.NONE
    }
}
