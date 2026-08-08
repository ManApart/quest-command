package traveling.climb

import core.thing.Thing
import traveling.direction.Direction
import traveling.direction.getDirectionTo
import traveling.location.CLIMBING
import traveling.location.Connection
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode

data class ClimbThing(val thing: Thing, val connection: Connection? = null, val exit: LocationPoint? = null) {
    private val start = if (connection != null && exit != null) connection.opposite(exit.location)?.location else null
    private val directionToExit = start?.getDirectionTo(exit!!.location)

    fun getDirection(climber: Thing): Direction {
        if (climber.location == thing.location) {
            if (climber.position.z < thing.position.z + thing.body.getHeight()) return Direction.ABOVE
            if (climber.position.z > thing.position.z) return Direction.BELOW
        }
        if (directionToExit != null) return directionToExit
        return Direction.NONE
    }

    fun getName(delim: String): String {
        val exitString = exit?.location?.name?.let { " $delim $it" } ?: ""
        return "${thing.name}$exitString"
    }
}

suspend fun LocationNode.determineClimbThings(): List<ClimbThing> {
    val connectionThings = getNeighborConnections().filter { connection ->
        connection.kind == CLIMBING
    }.mapNotNull { c ->
        (c.source.getThing() ?: c.destination.getThing())?.let { thing -> ClimbThing(thing, c, c.destination) }
    }
    val localThings = getLocation().findThingsByTag("Climbable").filter { connectionThings.none { ct -> ct.thing == it } }.map { ClimbThing(it) }

    return localThings + connectionThings
}
