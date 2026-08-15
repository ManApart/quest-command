package core

import core.body.BodyPart
import core.thing.Thing
import traveling.location.Route

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
    val body2 get() = thing.body2
    val properties get() = thing.properties
    val mind get() = thing.mind
    val ai get() = thing.mind.ai
    val inventory get() = thing.inventory
    val location get() = thing.location
    val position get() = thing.position

    suspend fun getPerceivedThings() = location.getLocation().getThings(perceivedBy = thing)
    suspend fun getPerceivedThingNames() = getPerceivedThings().map { it.name }
    suspend fun getPerceivedParts(): List<BodyPart> {
        return getPerceivedThings().toList().filter { thing.perceives(it) }.flatMap { thing -> thing.body2.parts }.toSet().toList()
    }

    suspend fun getPerceivedPartNames() = getPerceivedParts().toList().map { it.name }.toSet().toList()

}
