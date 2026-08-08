package traveling.direction

import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.Vector

enum class Direction(val shortcut: String, val vector: Vector, val aliases: List<String> = listOf()) {
    NORTH("n", Vector(y = 1)),
    SOUTH("s", Vector(y = -1)),
    WEST("w", Vector(x = -1)),
    EAST("e", Vector(x = 1)),
    NORTH_WEST("nw", Vector(-1, 1)),
    NORTH_EAST("ne", Vector(1, 1)),
    SOUTH_WEST("sw", Vector(-1, -1)),
    SOUTH_EAST("se", Vector(1, -1)),
    ABOVE("a", Vector(z = 1), listOf("up")),
    BELOW("d", Vector(z = -1), listOf("down", "bl")),
    NONE("none", Vector());

    val wordList = listOf(name.lowercase(), shortcut.lowercase()) + aliases.map { it.lowercase() }

    fun invert(): Direction {
        return vector.invert().direction
    }

    /**
     * EX: to the north west
     */
    fun directionString(): String {
        return when (this) {
            ABOVE -> this.name.lowercase()
            BELOW -> this.name.lowercase()
            NONE -> ""
            else -> "to the " + this.name.lowercase().replace("_", " ")
        }
    }

    companion object {
        fun getDirection(value: String): Direction {
            val cleaned = value.lowercase().trim()
            return entries.firstOrNull { it.wordList.contains(cleaned) } ?: NONE
        }
    }

}

fun Thing.getDirectionTo(other: Thing) = if (location == other.location) getDirectionToWithinLocation(other) else location.getDirectionTo(other.location)
private fun Thing.getDirectionToWithinLocation(other: Thing) = position.calculateDirection(other.position)
fun LocationNode.getDirectionTo(other: LocationNode) = getConnection(other)?.vector?.direction ?: Direction.NONE
