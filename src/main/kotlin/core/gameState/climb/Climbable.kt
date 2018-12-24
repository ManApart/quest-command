package core.gameState.climb

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import core.utility.apply
import system.location.LocationManager

class Climbable(
        val name: String,
        @JsonProperty("destination") val destinationName: String,
        val upwards: Boolean
) {
    constructor(base: Climbable, params: Map<String, String> = mapOf()) : this(base.name.apply(params), base.destinationName.apply(params), base.upwards)

    @JsonIgnore val destination = LocationManager.findLocation(destinationName)

}