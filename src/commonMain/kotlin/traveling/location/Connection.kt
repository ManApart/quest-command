package traveling.location

import traveling.location.location.LocationPoint

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

}