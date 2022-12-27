package traveling.location.location

import core.ai.knowledge.Subject
import core.thing.Thing
import core.utility.ParamBuilder
import core.utility.ParamBuilderI
import traveling.position.VectorParent
import traveling.position.VectorParentI

class LocationThingBuilder(
    private val name: String
) : VectorParent by VectorParentI(), ParamBuilder by ParamBuilderI() {
    private var location: String? = null
    private var transformations = mutableListOf<(Thing) -> Unit>()

    fun build(): LocationThing {
        val params = paramsBuilder.build()
        return LocationThing(name, location, vector, params) { thing -> transformations.forEach { it(thing) } }
    }

    fun location(location: String) {
        this.location = location
    }

    fun transform(transformation: (Thing) -> Unit) {
        transformations.add(transformation)
    }

    fun learnBedLocation(bedName: String, location: String, network: String? = null) {
        transformations.add { it.mind.learn(Subject(bedName, location, network), "MyBed") }
    }

    fun learnWorkLocation(location: String, network: String? = null) {
        transformations.add { it.mind.learn(Subject(null, location, network), "MyWorkplace") }
    }

    fun param(values: Map<String, Any>) = this.paramsBuilder.entry(values.toList())

}

fun locThing(name: String, initializer: LocationThingBuilder.() -> Unit): LocationThingBuilder {
    return LocationThingBuilder(name).apply(initializer)
}

fun List<LocationThingBuilder>.build(): List<LocationThing> {
    return map { it.build() }
}

fun LocationThing.unBuild(): LocationThingBuilder {
    return locThing(name) {
        location?.let { location(it) }
        vector(vector)
        param(params)
        transform(transform)
    }
}