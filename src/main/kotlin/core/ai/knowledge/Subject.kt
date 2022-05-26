package core.ai.knowledge

import core.thing.Thing
import traveling.location.network.LocationNode

typealias SubjectFilter = (Subject) -> Boolean

data class Subject(
    val name: String,
    val thing: Thing? = null,
    val location: LocationNode? = null,
    val propertyValue: String? = null,
    val propertyTag: String? = null
) {
    constructor(creature: Thing) : this(creature.name, creature)
    constructor(location: LocationNode) : this(location.name, location = location)

    init {
        require(thing != null || location != null) { "Thing or location most not be null!" }
    }
}

//This simplified subject is stored within facts and relationships
@kotlinx.serialization.Serializable
data class SimpleSubject(
    val thingName: String? = null,
    val locationName: String? = null,
    val topicName: String? = null,
) {
    constructor(b: Subject) : this(b.thing?.name, b.location?.name)
    constructor(b: Thing) : this(thingName = b.name)
    constructor(b: LocationNode) : this(locationName = b.name)
    constructor(b: String) : this(topicName = b)
}