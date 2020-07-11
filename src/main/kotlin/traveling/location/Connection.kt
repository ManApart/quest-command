package traveling.location

import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.location.location.LocationPoint

class Connection(val source: LocationPoint, val destination: LocationPoint, val vector: Vector = NO_VECTOR, var restricted: Boolean = false) {

    override fun toString(): String {
        return "Connection: ${source.getName()} - ${destination.getName()}"
    }

    fun invert(): Connection {
        return Connection(destination, source, vector.invert(), restricted)
    }

    fun isNetworkConnection() : Boolean {
        return source.location.parent != destination.location.parent
    }


}