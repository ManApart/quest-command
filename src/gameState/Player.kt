package gameState

class Player {
    var location = GameState.world.findLocation(listOf("an open field"))
    val items = mutableListOf<Item>()
    val soul = Soul()

}