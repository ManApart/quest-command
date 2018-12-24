package core.gameState.climb

import core.utility.apply
import system.location.LocationManager

class Climbable(val name: String, destinationName: String, val upwards: Boolean) {
    constructor(base: Climbable, params: Map<String, String> = mapOf()) : this(base.name.apply(params), base.destination.name.apply(params), base.upwards)

    val destination = LocationManager.findLocation(destinationName)

}