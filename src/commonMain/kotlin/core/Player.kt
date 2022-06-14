package core

import core.thing.Thing
import core.utility.NameSearchableList
import crafting.Recipe
import traveling.location.Route
import traveling.location.network.LocationNode

data class Player(
    val name: String,
    val thing: Thing
) {
    var compassRoute: Route? = null

    override fun equals(other: Any?): Boolean {
        return other is Player && name == other.name
    }

    override fun hashCode(): Int {
        return name.length
    }

    fun isPlayer(): Boolean {
        return thing.isPlayer()
    }

    val soul get() = thing.soul
    val body get() = thing.body
    val properties get() = thing.properties
    val mind get() = thing.mind
    val ai get() = thing.mind.ai
    val inventory get() = thing.inventory
    val location get() = thing.location
    val position get() = thing.position

    fun getPerceivedThingNames(): List<String> {
        return location.getLocation().getThings().filter { thing.perceives(it) }.map { it.name }
    }

    fun getPerceivedPartNames(): List<String> {
        return location.getLocation().getThings().filter { thing.perceives(it) }.flatMap { thing -> thing.body.getParts().map { it.name } }.toSet().toList()
    }
}
