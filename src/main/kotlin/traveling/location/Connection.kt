package traveling.location

import traveling.location.location.LocationPoint
import traveling.position.NO_VECTOR
import traveling.position.Vector

class Connection(val source: LocationPoint, val destination: LocationPoint, val originPoint: Vector = NO_VECTOR, val destinationPoint: Vector = NO_VECTOR, var restricted: Boolean = false, var hidden: Boolean = false) {
    /*
    The vector most count for inverting.
    0,-100,0 -> 0,0,0
    0,0,0 -> 0,-100,0

    0,0,15 -> 0,0,0
    0,0,0 -> 0,0,15
     */
    val vector = originPoint - destinationPoint

    override fun toString(): String {
        return "Connection: ${source.getName()} - ${destination.getName()}"
    }

    fun invert(): Connection {
        return Connection(destination, source, destinationPoint, originPoint, restricted, hidden)
    }

    fun isNetworkConnection() : Boolean {
        return source.location.parent != destination.location.parent
    }


}