package core.gameState

enum class Direction(val shortcut: String) {

    NORTH("n"), SOUTH("s"), WEST("w"), EAST("e"), NORTH_WEST("nw"), NORTH_EAST("ne"), SOUTH_WEST("sw"), SOUTH_EAST("se"), ABOVE("a"), BELOW("d"), NONE("none");



    companion object {
        fun getDirection(value: String) : Direction {
            val cleaned = value.toLowerCase().trim()
            return Direction.values().firstOrNull {
                cleaned == it.name.toLowerCase() || cleaned == it.shortcut.toLowerCase()
            } ?: Direction.NONE
        }
    }

}