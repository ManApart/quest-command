package gameState

class Player : Target, Creature{
    var location = GameState.world.findLocation(listOf("an open field"))
    val items = mutableListOf<Item>()
    override val soul = Soul()
    override val tags = Tags(listOf("Creature"))

    override fun toString(): String {
        return "Player"
    }


}