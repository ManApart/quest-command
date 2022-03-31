package status.stat

import core.thing.Thing
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

//Attributes
const val AGILITY = "Agility"
const val HEALTH = "Health"
const val PERCEPTION = "Perception"
const val STAMINA = "Stamina"
const val STRENGTH = "Strength"
const val WISDOM = "Wisdom"
const val FOCUS = "Focus"

//Skills
const val CLIMBING = "Climbing"
const val BARE_FOOT = "Barefoot"
const val BARE_HANDED = "Bare Handed"
const val COOKING = "Cooking"
const val CRAFTSMANSHIP = "Craftsmanship"
const val SMITHING = "Smithing"
const val SNEAK = "Sneak"

//Magic Skills
const val AIR_MAGIC = "AirMagic"
const val EARTH_MAGIC = "EarthMagic"
const val FIRE_MAGIC = "FireMagic"
const val WATER_MAGIC = "WaterMagic"

@kotlinx.serialization.Serializable(with = LeveledStatSerializer::class)
class LeveledStat(
    val name: String,
    level: Int = 1,
    private var maxMultiplier: Int = 1,
    val expExponential: Int = 2,
    max: Int? = null,
    current: Int? = null,
    xp: Double? = null,
    private val levelUp: (LeveledStat, Int) -> Unit = { _, _ -> },
    private val statMinned: (String) -> Unit = { },
    private val statMaxed: (String) -> Unit = { },
) {
    constructor(parent: Thing, base: LeveledStat) : this(base.name, base.level, base.maxMultiplier, base.expExponential)

    var level: Int = level; private set
    var max: Int = max ?: getBaseMaxAtCurrentLevel(); private set
    var current: Int = current ?: this.max; private set
    var xp: Double = xp ?: getXPAt(level); private set

    override fun toString(): String {
        return "$name: lvl $level, $current/$max"
    }

    fun copy(): LeveledStat {
        return LeveledStat(name, level, maxMultiplier, expExponential, max, current, xp)
    }

    fun addEXP(amount: Int) {
        if (amount > 0) {
            xp += amount

            val oldLevel = level
            determineLevel()
            if (level > oldLevel) {
                max++
                levelUp(this, level)
            }
        }
    }

    fun levelUp(times: Int = 1) {
        val xp = getXPAt(level + times)
        addEXP(xp.toInt())
    }

    fun incStat(amount: Int) {
        if (amount != 0) {
            val oldVal = current
            current = max(min(current + amount, max), 0)

            if (current != oldVal) {
                if (current == 0) {
                    statMinned(name)
                } else if (current == max) {
                    statMaxed(name)
                }
            }
        }
    }

    fun incStatMax(amount: Int) {
        if (amount != 0) {
            max = max(max + amount, 0)
            current = min(max, current)

            if (current == 0) {
                statMinned(name)
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

    fun isHealth(): Boolean {
        return name.lowercase().endsWith("health")
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
        return level.toDouble().pow(expExponential.toDouble())
    }

    fun getNextLevelXP(): Double {
        return getXPAt(level + 1)
    }

    fun getMaxMultiplier(): Int {
        return maxMultiplier
    }

}
