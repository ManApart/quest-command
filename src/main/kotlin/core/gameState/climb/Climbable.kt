package core.gameState.climb

import system.location.LocationManager

class Climbable(val name: String, destination: String, val upwards: Boolean) {
    val destination = LocationManager.findLocation(destination)

}