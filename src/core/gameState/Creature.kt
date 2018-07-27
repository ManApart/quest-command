package core.gameState

class Creature(override val name: String, override val description: String, val body: Body = Body(), var location: Location = GameState.world, val parent: Target? = null, override val properties: Properties = Properties()) : Target {
    val soul = Soul(this)
    val inventory = Inventory()

    init {
        properties.tags.add("Creature")
        soul.addStats(properties.stats)
    }
}