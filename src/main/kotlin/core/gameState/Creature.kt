package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator
import core.gameState.location.LocationNode
import system.AIManager
import system.BodyManager
import system.ItemManager
import system.LocationManager

class Creature(override val name: String, override val description: String, val body: Body = Body(), var location: LocationNode = LocationManager.NOWHERE_NODE, ai: String? = null, val parent: Target? = null, override val properties: Properties = Properties()) : Target {

    val soul = Soul(this)
    val inventory = Inventory()
    val ai = if (ai != null) AIManager.getAI(ai, this) else null

    @JsonCreator
    constructor(name: String, description: String, body: String, ai: String?, properties: Properties = Properties(), items: List<String>) : this(name, description, BodyManager.getBody(body), ai = ai, properties = properties) {
        items.forEach { inventory.add(ItemManager.getItem(it)) }
    }


    init {
        properties.tags.add("Creature")
        soul.addStats(properties.stats)
    }
}