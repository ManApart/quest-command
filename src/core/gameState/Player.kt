package core.gameState

class Player : Creature {
    var location = GameState.world.findLocation(listOf("an open field"))
    override val name = "Player"
    override val description = "Our Hero!"
    override val inventory: Inventory = Inventory()
    override val soul = Soul(mutableListOf(
            Stat(Stat.HEALTH, 100),
            Stat(Stat.STAMINA, 100),
            Stat(Stat.STRENGTH, 1),
            Stat(Stat.AGILITY, 1)))
    override val properties = Properties()

    init {
        properties.tags.add("Creature")
    }

    override fun toString(): String {
        return name
    }


}