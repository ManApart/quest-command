package combat.battle.position

enum class Horizontal(private val value: Int) {
    FAR_LEFT(-2),
    LEFT(-1),
    CENTER(0),
    RIGHT(1),
    FAR_RIGHT(2);

    override fun toString(): String {
        return name.toLowerCase()
    }

    fun isAdjacent(other: Horizontal): Boolean {
        return Math.abs(this.value - other.value) == 1
    }

    fun shift(other: Horizontal): Horizontal {
        return fromValue(this.value + other.value)
    }

    private fun fromValue(value: Int): Horizontal {
        val match = Horizontal.values().firstOrNull { it.value == value }
        val minValue = Horizontal.values().minBy { it.value }!!.value
        return when {
            match != null -> match
            value < minValue -> Horizontal.FAR_LEFT
            else -> Horizontal.FAR_RIGHT
        }
    }

    fun invert(): Horizontal {
        return fromValue(value * -1)
    }
}