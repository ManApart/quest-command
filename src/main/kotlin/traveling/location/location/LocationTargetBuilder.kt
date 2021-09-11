package traveling.location.location

import core.utility.ParamBuilder
import core.utility.ParamBuilderI
import traveling.position.VectorParent
import traveling.position.VectorParentI

class LocationTargetBuilder(
    private val name: String
) : VectorParent by VectorParentI(), ParamBuilder by ParamBuilderI() {
    private var location: String? = null

    fun build(): LocationTarget {
        val params = paramsBuilder.build()
        return LocationTarget(name, location, vector, params)
    }

    fun location(location: String) {
        this.location = location
    }

}

fun locTarget(name: String, initializer: LocationTargetBuilder.() -> Unit): LocationTargetBuilder {
    return LocationTargetBuilder(name).apply(initializer)
}

fun List<LocationTargetBuilder>.build() : List<LocationTarget>{
    return map { it.build() }
}