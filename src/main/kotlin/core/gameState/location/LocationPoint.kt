package core.gameState.location

import core.gameState.Target
import core.gameState.body.BodyPart

class LocationPoint(val location: LocationNode, private val targetName: String? = null, private val partName: String? = null) {

    fun getName(): String {
        return when {
            targetName != null && partName != null -> "${location.name}: $partName of $targetName"
            targetName != null -> "${location.name}: $targetName"
            else -> location.name
        }
    }

    fun equals(location: LocationNode, target: Target?, part: BodyPart?): Boolean {
        return location == this.location
                && (target == null || target.name == targetName)
                && (part == null || part.name == partName)
    }
}