package traveling.position

import core.thing.Thing
import traveling.location.location.Location

class ThingAim(val thing: Thing, val bodyPartThings: List<Location> = listOf()) {
    override fun toString(): String {
        return if (bodyPartThings.isEmpty()) {
            thing.toString()
        } else {
            bodyPartThings.joinToString(" ") + " of " + thing.toString()
        }
    }

    fun toCommandString(): String {
        return bodyPartThings.joinToString(" ") { it.name } + " of " + thing.name
    }
}

fun List<ThingAim>.toCommandString(): String {
    return joinToString(" and ") { it.toCommandString() }
}

