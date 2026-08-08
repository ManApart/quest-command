package traveling.climb

import core.thing.Thing
import traveling.location.CLIMBING
import traveling.location.Connection
import traveling.location.network.LocationNode

data class ClimbThing(val thing: Thing, val connection: Connection? = null, val exit: LocationNode? = null)


suspend fun LocationNode.determineClimbThings(): List<ClimbThing>{
    val connectionThings = getNeighborConnections().filter { connection ->
        connection.kind == CLIMBING
    }.mapNotNull { c ->
        (c.destination.getThing() ?: c.source.getThing())?.let { thing -> ClimbThing(thing, c, c.destination.location) }
    }
    val localThings = getLocation().findThingsByTag("Climbable").filter { connectionThings.none { ct -> ct.thing == it } }.map { ClimbThing(it) }

    return localThings + connectionThings
}
