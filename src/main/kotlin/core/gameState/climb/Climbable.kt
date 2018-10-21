package core.gameState.climb

import core.gameState.GameState

class Climbable(val name: String, destination: String, val upwards: Boolean) {
    val destination = GameState.world.findLocation(destination.split(" "))

}