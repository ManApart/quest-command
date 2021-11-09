package traveling.location.location

import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

class LocationPoint(val location: LocationNode, val vector: Vector = NO_VECTOR, val thingName: String? = null, val partName: String? = null) {

    override fun toString(): String {
        return getName()
    }

    fun getName(): String {
        return when {
            thingName != null && partName != null -> "${location.name}: $partName of $thingName"
            thingName != null -> "${location.name}: $thingName"
            else -> location.name
        }
    }

    fun equals(location: LocationNode, thing: Thing?, part: Location?): Boolean {
        return location == this.location
                && (thing == null || thing.name == thingName)
                && (part == null || part.name == partName)
    }

    fun hasThingAndPart() : Boolean {
        return !thingName.isNullOrBlank() && !partName.isNullOrBlank()
    }
}