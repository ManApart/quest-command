package core.gameState.location

import core.gameState.NO_POSITION
import core.gameState.Position

class LocationLink(val source: LocationNode, val destination: LocationNode, val position: Position = NO_POSITION, var restricted: Boolean = false) {

    override fun toString(): String {
        return "Location Link: ${source.name} - ${destination.name}"
    }

    fun invert() : LocationLink {
        return LocationLink(destination, source, position.invert(), restricted)
    }
}