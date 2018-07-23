package core.gameState.stat

import core.gameState.Creature
import status.LevelUpEvent
import system.EventManager

class Stat(val name: String, level: Int = 1, private var maxMultiplier: Int = 1, val expExponential: Int = 2) {
    var level = level; private set
    var max: Int = calcMax()
    var current: Int = max
    var exp: Double = getEXPAt(level); private set

    fun addEXP(amount: Int, creature: Creature) {
        if (amount > 0) {
            exp += amount

            val oldLevel = level
            determineLevel()
            if (level > oldLevel) {
                max = calcMax()
                EventManager.postEvent(LevelUpEvent(creature, this, level))
            }
        }
    }

    private fun determineLevel() {
        while (exp >= getNextLevelEXP()) {
            level++
        }
    }

    private fun calcMax(): Int {
        return level * maxMultiplier
    }

    private fun getEXPAt(level: Int): Double {
        return Math.pow(level.toDouble(), expExponential.toDouble())
    }

    fun getNextLevelEXP(): Double {
        return getEXPAt(level + 1)
    }

    companion object {
        //Attributes
        const val AGILITY = "Agility"
        const val HEALTH = "Health"
        const val STAMINA = "Stamina"
        const val STRENGTH = "Strength"

        //Skills
        const val CLIMBING = "Climbing"
        const val BARE_FOOT = "Barefoot"
    }


}