package traveling.location.location

import traveling.position.NO_VECTOR
import traveling.position.Vector

class LocationThing(val name: String, val location: String? = null, val position: Vector = NO_VECTOR, val params: Map<String, String> = mapOf()) {
    constructor(thingName: String) : this(thingName, null, NO_VECTOR, mapOf())

    override fun toString(): String {
        return "$name at $position of $location"
    }
}