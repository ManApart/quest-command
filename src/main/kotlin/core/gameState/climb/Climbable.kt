package core.gameState.climb

import system.LocationManager

class Climbable(val name: String, destination: String, val upwards: Boolean) {
    val destination = LocationManager.findLocation(destination)

}