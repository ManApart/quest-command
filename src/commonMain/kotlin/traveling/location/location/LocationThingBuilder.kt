package traveling.location.location

import core.utility.ParamBuilder
import core.utility.ParamBuilderI
import traveling.position.VectorParent
import traveling.position.VectorParentI

class LocationThingBuilder(
    private val name: String
) : VectorParent by VectorParentI(), ParamBuilder by ParamBuilderI() {
    private var location: String? = null

    fun build(): LocationThing {
        val params = paramsBuilder.build()
        return LocationThing(name, location, vector, params)
    }

    fun location(location: String) {
        this.location = location
    }

}

fun locThing(name: String, initializer: LocationThingBuilder.() -> Unit): LocationThingBuilder {
    return LocationThingBuilder(name).apply(initializer)
}

fun List<LocationThingBuilder>.build() : List<LocationThing>{
    return map { it.build() }
}