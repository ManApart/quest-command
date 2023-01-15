package core.ai.knowledge

import core.thing.Thing
import kotlinx.serialization.Transient
import traveling.location.location.LocationManager
import traveling.location.network.LocationNode

@kotlinx.serialization.Serializable
data class Subject(
    val thingName: String? = null,
    val locationName: String? = null,
    val networkName: String? = null,
    val topic: String? = null,
) {
    constructor(b: Thing) : this(b.name, b.location.name, b.location.network.name)
    constructor(b: LocationNode) : this(locationName = b.name, networkName = b.network.name)
    constructor(b: String) : this(topic = b)

    init {
        require(thingName != null || locationName != null || topic != null) { "Thing, location or topic must not be null!" }
    }

    override fun toString(): String {
        return listOfNotNull(
            thingName?.let { "Thing: $it" },
            locationName?.let { "Location: $it" },
            networkName?.let { "Network: $it" },
            topic?.let { "Topic: $it" },
        ).joinToString(", ")
    }

    @Transient
    private var thing: Thing? = null
    suspend fun getThing(): Thing? {
        if (thing == null && networkName != null && locationName != null && thingName != null){
            thing = LocationManager.getNetwork(networkName).findLocation(locationName).getLocation().getThings(thingName).firstOrNull()
        }
        return thing
    }

    @Transient
    private var location: LocationNode? = null
    fun getLocation(): LocationNode? {
        if (location == null && networkName != null && locationName != null){
            location = LocationManager.getNetwork(networkName).findLocation(locationName)
        }
        return location
    }

}