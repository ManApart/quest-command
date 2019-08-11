package core.gameState

import core.gameState.stat.Stat
import core.utility.NameSearchableList
import status.effects.Condition
import status.effects.ElementInteraction

class Soul(val parent: Target, private val stats: MutableList<Stat> = mutableListOf()) {
    private val conditions = NameSearchableList<Condition>()

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

    fun addStats(stats: Map<String, String>) {
        stats.forEach { addStat(it.key, Integer.parseInt(it.value)) }
    }

    fun addStat(name: String, level: Int = 1, maxMultiplier: Int = 1, expExponential: Int = 2) {
        stats.add(Stat(name, parent, level, maxMultiplier, expExponential))
    }

    fun hasStat(name: String): Boolean {
        return getStatOrNull(name) != null
    }

    fun hasCondition(name: String): Boolean {
        return conditions.exists(name)
    }

    fun getConditions() : List<Condition> {
        return conditions.toList()
    }

    fun getCondition(name: String) : Condition {
        return conditions.get(name)
    }

    fun getCurrent(name: String, default: Int = 0): Int {
        return getStatOrNull(name)?.current ?: default
    }

    fun getTotal(name: String, default: Int = 0): Int {
        return getStatOrNull(name)?.max ?: default
    }

    private fun getOrCreateStat(name: String): Stat {
        if (getStatOrNull(name) == null) {
            addStat(name)
        }
        return getStatOrNull(name)!!
    }

    fun getStatOrNull(name: String): Stat? {
        return stats.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getStats(): List<Stat> {
        return stats.toList()
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

}