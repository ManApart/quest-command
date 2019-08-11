package core.gameState.stat

import combat.DamageType
import core.gameState.Target
import status.LevelUpEvent
import status.effects.Element
import status.statChanged.StatMaxedEvent
import status.statChanged.StatMinnedEvent
import system.EventManager

//Attributes
const val AGILITY = "Agility"
const val HEALTH = "Health"
const val PERCEPTION = "Perception"
const val STAMINA = "Stamina"
const val STRENGTH = "Strength"
const val WISDOM = "Wisdom"

//Skills
const val CLIMBING = "Climbing"
const val BARE_FOOT = "Barefoot"
const val BARE_HANDED = "Bare Handed"
const val COOKING = "Cooking"

class Stat(val name: String, private val parent: Target, private var level: Int = 1, private var maxMultiplier: Int = 1, val expExponential: Int = 2) {
    var max: Int = getBaseMaxAtCurrentLevel(); private set
    var current: Int = max; private set
    private var xp: Double = getXPAt(level)

    fun addEXP(amount: Int) {
        if (amount > 0) {
            xp += amount

            val oldLevel = level
            determineLevel()
            if (level > oldLevel) {
                max++
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
            current = Math.max(Math.min(current + amount, max), 0)

            if (current == 0) {
                EventManager.postEvent(StatMinnedEvent(parent, name))
            } else if (current == max) {
                EventManager.postEvent(StatMaxedEvent(parent, name))
            }
        }
    }

    fun incStatMax(amount: Int) {
        if (amount != 0) {
            max = Math.max(max + amount, 0)
            current = Math.min(max, current)

            if (current == 0) {
                EventManager.postEvent(StatMinnedEvent(parent, name))
            }
        }
    }

    fun setLevel(desiredLevel: Int) {
        xp = 0.toDouble()
        level = 0
        max = 0
        current = 0
        levelUp(desiredLevel)
        incStatMax(desiredLevel)
        incStat(desiredLevel)
    }

    fun isHealth() : Boolean {
        return name.toLowerCase().endsWith("health")
    }

    private fun determineLevel() {
        while (xp >= getNextLevelXP()) {
            level++
        }
    }

    fun getBaseMaxAtCurrentLevel(): Int {
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

}
