package traveling.position

import core.body.BodyPart
import core.thing.Thing

class ThingAim(val thing: Thing, val parts: List<BodyPart> = listOf()) {
    override fun toString(): String {
        return if (parts.isEmpty()) {
            thing.toString()
        } else {
            parts.joinToString(" ") + " of " + thing.toString()
        }
    }

    fun toCommandString(): String {
        return parts.joinToString(" ") { it.name } + " of " + thing.name
    }

    fun isLookingAtBody(): Boolean {
        return this.thing.body.parts.size == this.parts.size
    }
}

fun List<ThingAim>.toCommandString(): String {
    return joinToString(" and ") { it.toCommandString() }
}
