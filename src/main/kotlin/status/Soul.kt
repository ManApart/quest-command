package status

import core.events.EventManager
import core.thing.Thing
import core.utility.NameSearchableList
import magic.ElementInteraction
import status.conditions.Condition
import status.stat.LeveledStat
import status.statChanged.StatMaxedEvent
import status.statChanged.StatMinnedEvent

data class Soul(private val leveledStats: MutableList<LeveledStat> = mutableListOf()) {
    constructor(stats: Map<String, Int>) : this() {
        addStats(stats)
    }

    lateinit var parent: Thing
    private val conditions = NameSearchableList<Condition>()

    fun copy(): Soul {
        val newStats = leveledStats.map { it.copy() }.toMutableList()
        return Soul(newStats)
    }

    fun resetStatsAndConditions() {
        leveledStats.forEach { it.resetCurrent() }
        conditions.clear()
    }

    private fun levelUp(stat: LeveledStat, newLevel: Int) {
        EventManager.postEvent(LevelUpEvent(parent, stat, newLevel))
    }

    private fun statMinned(stat: String) {
        EventManager.postEvent(StatMinnedEvent(parent, stat))
    }

    private fun statMaxed(stat: String) {
        EventManager.postEvent(StatMaxedEvent(parent, stat))
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
        leveledStats.add(LeveledStat(name, level, maxMultiplier, expExponential, levelUp = ::levelUp, statMinned = ::statMinned, statMaxed = ::statMaxed))
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

    fun overrideConditions(newConditions: List<Condition>) {
        conditions.clear()
        conditions.addAll(newConditions)
    }

}