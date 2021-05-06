package status

import core.target.Target
import core.utility.NameSearchableList
import magic.ElementInteraction
import status.conditions.Condition
import status.stat.LeveledStat

class Soul(val parent: Target, leveledStats: List<LeveledStat> = listOf(), stats: Map<String, Int> = mapOf()) {
    private val leveledStats = leveledStats.map { LeveledStat(parent, it) }.toMutableList()
    private val conditions = NameSearchableList<Condition>()

    init {
        addStats(stats)
    }

    fun incStat(name: String, amount: Int) {
        if (amount != 0) {
            val stat = getOrCreateStat(name)
            stat.incStat(amount)
        }
    }

    fun incStatMax(name: String, amount: Int) {
        if (amount != 0) {
            val stat = getOrCreateStat(name)
            stat.incStatMax(amount)
        }
    }

    /**
     * Forcibly sets a stat to a specific level. Should only be used for debug purposes.
     */
    fun setStat(name: String, level: Int) {
        val stat = getStatOrNull(name)
        if (stat != null) {
            leveledStats.remove(stat)
        }
        addStat(name, level, stat?.getMaxMultiplier() ?: 1, stat?.expExponential ?: 2)
    }

    fun addStats(stats: Map<String, Int>) {
        stats.forEach { addStat(it.key, it.value) }
    }

    fun addStat(name: String, level: Int = 1, maxMultiplier: Int = 1, expExponential: Int = 2) {
        leveledStats.add(LeveledStat(name, parent, level, maxMultiplier, expExponential))
    }

    fun hasStat(name: String): Boolean {
        return getStatOrNull(name) != null
    }

    fun hasCondition(condition: Condition): Boolean {
        return conditions.contains(condition)
    }

    fun hasCondition(name: String): Boolean {
        return conditions.exists(name)
    }

    fun hasEffect(name: String): Boolean {
        return conditions.any { it.hasEffect(name) }
    }

    fun getConditions(): List<Condition> {
        return conditions.toList()
    }

    fun getCondition(name: String): Condition {
        return conditions.get(name)
    }

    fun getConditionWithEffect(name: String): Condition {
        return conditions.first { it.hasEffect(name) }
    }

    fun getCurrent(name: String, default: Int = 0): Int {
        return getStatOrNull(name)?.current ?: default
    }

    fun getTotal(name: String, default: Int = 0): Int {
        return getStatOrNull(name)?.max ?: default
    }

    private fun getOrCreateStat(name: String): LeveledStat {
        if (getStatOrNull(name) == null) {
            addStat(name)
        }
        return getStatOrNull(name)!!
    }

    fun getStatOrNull(name: String?): LeveledStat? {
        return if (name == null) {
            null
        } else {
            leveledStats.firstOrNull { it.name.lowercase() == name.lowercase() }
        }
    }

    fun getStats(): List<LeveledStat> {
        return leveledStats.toList()
    }

    fun applyConditions() {
        conditions.toList().forEach {
            it.apply(this)
        }
    }

    fun addNewCondition(newCondition: Condition) {
        val existingCondition = conditions.firstOrNull { existingCondition ->
            newCondition.getReaction(existingCondition) != ElementInteraction.NONE
        }

        if (existingCondition != null) {
            when (newCondition.getReaction(existingCondition)) {
                ElementInteraction.STRONGER -> {
                    removeCondition(existingCondition)
                    conditions.add(newCondition)
                }
                ElementInteraction.WEAKER -> {
                    existingCondition.elementStrength -= newCondition.elementStrength
                }
                ElementInteraction.CRITICAL -> {
                    newCondition.isCritical = true
                    conditions.add(newCondition)
                }
                ElementInteraction.REVERSE_CRITICAL -> {
                    newCondition.isCritical = true
                }
                else -> {
                    conditions.add(newCondition)
                }
            }
        } else {
            conditions.add(newCondition)
        }
    }

    fun removeCondition(condition: Condition) {
        condition.removeEffects(this)
        conditions.remove(condition)
    }

    fun overrideStats(newStats: List<LeveledStat>) {
        leveledStats.clear()
        leveledStats.addAll(newStats)
    }

    fun overrideConditions(newConditions: List<Condition>) {
        conditions.clear()
        conditions.addAll(newConditions)
    }

}