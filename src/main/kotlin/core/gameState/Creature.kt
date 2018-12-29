package core.gameState

import com.fasterxml.jackson.annotation.JsonCreator
import core.gameState.location.LocationNode
import core.utility.apply
import system.AIManager
import system.BodyManager
import system.ItemManager
import system.location.LocationManager

class Creature(
        override val name: String,
        override val description: String,
        val body: Body = Body(),
        var location: LocationNode = LocationManager.NOWHERE_NODE,
        ai: String? = null,
        val parent: Target? = null,
        val inventory: Inventory = Inventory(),
        override val properties: Properties = Properties()
) : Target {

    @JsonCreator
    constructor(
            name: String,
            description: String,
            body: String, ai: String?,
            properties: Properties = Properties(),
            items: List<String>
    ) : this(name, description, BodyManager.getBody(body), ai = ai, inventory = Inventory(items), properties = properties)

    constructor(base: Creature, params: Map<String, String> = mapOf()) : this(
            base.name.apply(params),
            base.description.apply(params),
            base.body,
            base.location,
            base.ai?.name,
            base.parent,
            base.inventory,
            Properties(base.properties, params)
    )

    val soul = Soul(this)
    val ai = if (ai != null) AIManager.getAI(ai, this) else null


    init {
        properties.tags.add("Creature")
        soul.addStats(properties.stats.getAll())
    }

    override fun toString(): String {
        return name
    }

    fun getTotalCapacity(): Int {
        return if (soul.hasStat("Strength")){
            soul.getCurrent("Strength") * 10
        } else {
            properties.values.getInt("Capacity", 0)
        }
    }

}