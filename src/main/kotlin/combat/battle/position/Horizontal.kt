package combat.battle.position

enum class Horizontal(private val value: Int) {
    LEFT(-1), CENTER(0), RIGHT(1);

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
        return when {
            value <= -1 -> LEFT
            value >= 1 -> RIGHT
            else -> CENTER
        }
    }
}