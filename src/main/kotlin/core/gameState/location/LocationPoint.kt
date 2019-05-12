package core.gameState.location

import core.gameState.Target
import core.gameState.body.BodyPart

class LocationPoint(val location: LocationNode, val target: Target? = null, val part: BodyPart? = null) {

    fun getName(): String {
        return when {
            target != null && part != null -> "${location.name}: ${part.name} of ${target.name}"
            target != null -> "${location.name}: ${target.name}"
            else -> location.name
        }

    }
}