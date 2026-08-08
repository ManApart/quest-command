package traveling.location.location

import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

class LocationPoint(val location: LocationNode, val vector: Vector = NO_VECTOR, val thingName: String? = null) {

    override fun toString(): String {
        return getName()
    }

    fun getName(): String {
        return when {
            thingName != null -> "${location.name}: $thingName"
            vector != NO_VECTOR -> "$vector of ${location.name}"
            else -> location.name
        }
    }

    fun equals(location: LocationNode, thing: Thing?): Boolean {
        return location == this.location
                && (thing == null || thing.name == thingName)
    }

    suspend fun getThing(): Thing? {
        return thingName?.let { location.getLocation().getThings(it)}?.firstOrNull()
    }
}
