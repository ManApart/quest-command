package combat.battle.position

import core.gameState.Item
import core.gameState.Tags

enum class TargetDistance(private val distance: Int) {
    DAGGER(0),
    SWORD(1),
    SPEAR(2),
    BOW(3);

    fun closer(): TargetDistance {
        return fromValue(this.distance - 1)
    }

    fun farther(): TargetDistance {
        return fromValue(this.distance + 1)
    }

    private fun fromValue(value: Int): TargetDistance {
        return when {
            value <= 0 -> this
            value >= greatestValue() -> this
            else -> values().first { it.distance == value }
        }
    }

    private fun greatestValue(): Int {
        return values().maxBy { it.distance }?.distance ?: 0
    }

}