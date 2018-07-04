package gameState

class Player : Target{
    var location = GameState.world.findLocation(listOf("an open field"))
    val items = mutableListOf<Item>()
    val soul = Soul()

    override fun toString(): String {
        return "Your Self"
    }


}