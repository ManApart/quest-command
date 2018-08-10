package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator
import system.BodyManager
import system.ItemManager

class Creature(override val name: String, override val description: String, val body: Body = Body(), var location: Location = GameState.world, val parent: Target? = null, override val properties: Properties = Properties()) : Target {

    val soul = Soul(this)
    val inventory = Inventory()

    @JsonCreator
    constructor(name: String, description: String, body: String, properties: Properties = Properties(), items: List<String>): this(name, description, BodyManager.getBody(body), properties = properties) {
        items.forEach { inventory.add(ItemManager.getItem(it)) }
    }



    init {
        properties.tags.add("Creature")
        soul.addStats(properties.stats)
    }
}