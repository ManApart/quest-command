package core.gameState.location

import core.gameState.NO_POSITION
import core.gameState.Position

class Connection(val source: LocationPoint, val destination: LocationPoint, val vector: Position = NO_POSITION, var restricted: Boolean = false) {

    override fun toString(): String {
        return "Connection: ${source.getName()} - ${destination.getName()}"
    }

    fun invert(): Connection {
        return Connection(destination, source, vector.invert(), restricted)
    }
}