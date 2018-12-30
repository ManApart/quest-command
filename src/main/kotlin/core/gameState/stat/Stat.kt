package core.gameState.stat

import core.gameState.Target
import status.LevelUpEvent
import status.statChanged.StatMaxedEvent
import status.statChanged.StatMinnedEvent
import system.EventManager

class Stat(val name: String, private val parent: Target, private var level: Int = 1, private var maxMultiplier: Int = 1, val expExponential: Int = 2) {
    var baseMax: Int = calcMax(); private set
    var boostedMax = baseMax; private set
    var current: Int = boostedMax; private set
    private var xp: Double = getXPAt(level)

    fun addEXP(amount: Int) {
        if (amount > 0) {
            xp += amount

            val oldLevel = level
            determineLevel()
            if (level > oldLevel) {
                baseMax = calcMax()
                boostedMax = baseMax
                EventManager.postEvent(LevelUpEvent(parent, this, level))
            }
        }
    }

    fun levelUp(times: Int = 1) {
        val xp = getXPAt(level + times)
        addEXP(xp.toInt())
    }

    fun incStat(amount: Int) {
        if (amount != 0) {
            current = Math.max(Math.min(current + amount, boostedMax), 0)

            if (current == 0) {
                EventManager.postEvent(StatMinnedEvent(parent, name))
            } else if (current == boostedMax) {
                EventManager.postEvent(StatMaxedEvent(parent, name))
            }
        }
    }

    fun incStatMax(amount: Int) {
        if (amount != 0) {
            boostedMax = Math.max(boostedMax + amount, 0)
            current = Math.min(boostedMax, current)

            if (current == 0) {
                EventManager.postEvent(StatMinnedEvent(parent, name))
            }
        }
    }

    fun setLevel(desiredLevel: Int) {
        xp = 0.toDouble()
        level = 0
        baseMax = 0
        current = 0
        boostedMax = 0
        levelUp(desiredLevel)
        incStatMax(desiredLevel)
        incStat(desiredLevel)
    }

    private fun determineLevel() {
        while (xp >= getNextLevelXP()) {
            level++
        }
    }

    private fun calcMax(): Int {
        return level * maxMultiplier
    }

    private fun getXPAt(level: Int): Double {
        return Math.pow(level.toDouble(), expExponential.toDouble())
    }

    fun getCurrentXP() : Double {
        return xp
    }

    fun getNextLevelXP(): Double {
        return getXPAt(level + 1)
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
        const val BARE_HANDED = "Bare Handed"
        const val COOKING = "Cooking"
    }


}
