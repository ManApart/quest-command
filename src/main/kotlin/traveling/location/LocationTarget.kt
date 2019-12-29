package traveling.location

import traveling.direction.NO_VECTOR
import traveling.direction.Vector

class LocationTarget(val name: String, val location: String? = null, val position: Vector = NO_VECTOR, val params: Map<String, String> = mapOf()) {
    constructor(targetName: String) : this(targetName, null, NO_VECTOR, mapOf())
}