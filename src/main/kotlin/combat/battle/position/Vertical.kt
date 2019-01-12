package combat.battle.position

enum class Vertical(private val value: Int) {
    LOW(-1), CENTER(0), HIGH(1);

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
        return when {
            value <= -1 -> LOW
            value >= 1 -> HIGH
            else -> CENTER
        }
    }

}