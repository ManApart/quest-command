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
    constructor(creature: Thing): this(creature.name, creature)
    constructor(location: LocationNode): this(location.name, location = location)
    init {
        require(thing != null || location != null) { "Thing or location most not be null!" }
    }
}