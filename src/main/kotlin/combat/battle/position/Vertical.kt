package combat.battle.position

enum class Vertical(private val value: Int) {
    VERY_LOW(-2),
    LOW(-1),
    CENTER(0),
    HIGH(1),
    VERY_HIGH(2);

    override fun toString(): String {
        return name.toLowerCase()
    }

    fun isAdjacent(other: Vertical): Boolean {
        return Math.abs(this.value - other.value) == 1
    }

    fun shift(other: Vertical): Vertical {
        return fromValue(this.value + other.value)
    }

    private fun fromValue(value: Int): Vertical {
        val match = Vertical.values().firstOrNull { it.value == value }
        val minValue = Vertical.values().minBy { it.value }!!.value
        return when {
            match != null -> match
            value < minValue -> Vertical.VERY_LOW
            else -> Vertical.VERY_HIGH
        }
    }

    fun invert(): Vertical {
        return fromValue(value * -1)
    }

}