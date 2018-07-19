package core.gameState.climb

import core.gameState.Direction
import core.gameState.GameState

class Climbable(val name: String, destination: String, val direction: Direction) {
    val destination = GameState.world.findLocation(destination.split(" "))

}