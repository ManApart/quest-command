package core.gameState.location

import core.gameState.NO_VECTOR
import core.gameState.Vector

class LocationTarget(val name: String, val location: String? = null, val position: Vector = NO_VECTOR, val params: Map<String, String> = mapOf()) {
    constructor(targetName: String) : this(targetName, null, NO_VECTOR, mapOf())
}