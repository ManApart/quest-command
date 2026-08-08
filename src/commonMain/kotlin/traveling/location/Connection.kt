package traveling.location

import core.thing.Thing
import core.thing.thing
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode

class Connection(val source: LocationPoint, val destination: LocationPoint, val kind: String? = null, var restricted: Boolean = false, var hidden: Boolean = false) {
    /*
    The vector most count for inverting.
    0,-100,0 -> 0,0,0
    0,0,0 -> 0,-100,0

    0,0,15 -> 0,0,0
    0,0,0 -> 0,0,15
     */
    val vector = source.vector - destination.vector

    override fun toString(): String {
        return "Connection: ${source.getName()} - ${destination.getName()}"
    }

    fun invert(): Connection {
        return Connection(destination, source, kind, restricted, hidden)
    }

    fun has(thing: Thing): Boolean {
        return source.thingName == thing.name || destination.thingName == thing.name
    }

    fun opposite(end: LocationNode): LocationPoint? {
        return when {
            source.location == end -> destination
            destination.location ==end -> source
            else -> null
        }
    }
//    fun opposite(end: LocationNode, thing: Thing? = null): LocationPoint? {
//        return when {
//            source.equals(end, thing) -> destination
//            destination.equals(end, thing) -> source
//            else -> null
//        }
//    }

    fun endWith(thing: Thing): LocationPoint? {
        return source.takeIf { it.thingName == thing.name }
            ?: destination.takeIf { it.thingName == thing.name }
    }

}

//TODO

data class ConnectedThing(val connection: Connection, val source: Thing?, val dest: Thing?)

/*
At start, get list of connected things (by kind, like climbing)
Climbthing should prefer local to far thing
Get opposite location for direction calculation
 */

/*
Maybe ConnectedThing(thing: Thing, connectsTo :LocationNode?)
local only things don't have a location, else it's the location the thing connects to (NOt current location).
Then all our comparisons are player location vs connectedThing's connects to
if thing is here, connectsTo is its destination
if thing is in in other location, then it's things location
 */
