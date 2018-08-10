package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator
import system.BodyManager

class Creature(override val name: String, override val description: String, val body: Body = Body(), var location: Location = GameState.world, val parent: Target? = null, override val properties: Properties = Properties()) : Target {

    @JsonCreator
    constructor(name: String, description: String, body: String, properties: Properties = Properties()): this(name, description, BodyManager.getBody(body), properties = properties)

    val soul = Soul(this)
    val inventory = Inventory()

    init {
        properties.tags.add("Creature")
        soul.addStats(properties.stats)
    }
}