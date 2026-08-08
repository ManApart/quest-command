package traveling.climb

import core.thing.Thing
import traveling.direction.Direction
import traveling.direction.getDirectionTo
import traveling.location.CLIMBING
import traveling.location.Connection
import traveling.location.network.LocationNode

data class ClimbThing(val thing: Thing, val connection: Connection? = null, val exit: LocationNode? = null) {
    private val start = if (connection != null && exit != null) connection.opposite(exit)?.location else null
    private val direction = start?.getDirectionTo(exit!!)

    fun getDirection(climber: Thing): Direction {
        return if (direction != null) direction else {
            if (climber.location == thing.location){
                if (climber.position.y < thing.position.y + thing.body.getHeight()) Direction.ABOVE
                if (climber.position.y > thing.position.y) Direction.BELOW
            }
            Direction.NONE
        }
    }
}


suspend fun LocationNode.determineClimbThings(): List<ClimbThing> {
    val connectionThings = getNeighborConnections().filter { connection ->
        connection.kind == CLIMBING
    }.mapNotNull { c ->
        (c.destination.getThing() ?: c.source.getThing())?.let { thing -> ClimbThing(thing, c, c.destination.location) }
    }
    val localThings = getLocation().findThingsByTag("Climbable").filter { connectionThings.none { ct -> ct.thing == it } }.map { ClimbThing(it) }

    return localThings + connectionThings
}
