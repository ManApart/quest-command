package traveling.direction

import traveling.position.Vector

enum class Direction(val shortcut: String, val vector: Vector) {

    NORTH("n", Vector(y = 1)),
    SOUTH("s", Vector(y = -1)),
    WEST("w", Vector(x = -1)),
    EAST("e", Vector(x = 1)),
    NORTH_WEST("nw", Vector(-1, 1)),
    NORTH_EAST("ne", Vector(1, 1)),
    SOUTH_WEST("sw", Vector(-1, -1)),
    SOUTH_EAST("se", Vector(1, -1)),
    ABOVE("a", Vector(z = 1)),
    BELOW("d", Vector(z = -1)),
    NONE("none", Vector());

    fun invert(): Direction {
        return vector.invert().direction
    }

    companion object {
        fun getDirection(value: String) : Direction {
            val cleaned = value.lowercase().trim()
            return values().firstOrNull {
                cleaned == it.name.lowercase() || cleaned == it.shortcut.lowercase()
            } ?: NONE
        }
    }

}