package core.ai.knowledge

import core.thing.Thing
import traveling.location.network.LocationNode

typealias SubjectFilter = (Subject) -> Boolean

data class Subject(
    val thing: Thing? = null,
    val location: LocationNode? = null,
    val topic: String? = null,
    val propertyValue: String? = null,
    val propertyTag: String? = null
) {
    constructor(location: LocationNode) : this(null, location)
    constructor(topic: String) : this(null, null, topic)

    init {
        require(thing != null || location != null || topic != null) { "Thing or location most not be null!" }
    }

    override fun toString(): String {
        return listOfNotNull(
            thing?.let { "Thing: ${it.name}" },
            location?.let { "Location: ${it.name}" },
            topic?.let { "Topic: $it" },
            propertyValue?.let { "PropVal: $it" },
            propertyTag?.let { "Tag: $it" },
        ).joinToString(", ")
    }
}

//This simplified subject is stored within facts and relationships
@kotlinx.serialization.Serializable
data class SimpleSubject(
    val thingName: String? = null,
    val locationName: String? = null,
    val networkName: String? = null,
    val topic: String? = null,
) {
    constructor(b: Subject) : this(b.thing?.name, b.location?.name, b.location?.network?.name, b.topic)
    constructor(b: Thing) : this(b.name, b.location.name, b.location.network.name)
    constructor(b: LocationNode) : this(locationName = b.name, networkName = b.network.name)
    constructor(b: String) : this(topic = b)

    override fun toString(): String {
        return listOfNotNull(
            thingName?.let { "Thing: $it" },
            locationName?.let { "Location: $it" },
            networkName?.let { "Network: $it" },
            topic?.let { "Topic: $it" },
        ).joinToString(", ")
    }
}