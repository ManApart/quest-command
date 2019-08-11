package core.gameState

import core.gameState.stat.Stat
import interact.magic.Condition
import interact.magic.ElementInteraction

class Soul(val parent: Target, private val stats: MutableList<Stat> = mutableListOf()) {
    var effects = mutableListOf<Effect>()
    val conditions = mutableListOf<Condition>()

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

    fun hasEffect(name: String): Boolean {
        return getEffectOrNull(name) != null
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

    fun getEffectOrNull(name: String): Effect? {
        return effects.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getStats() : List<Stat> {
        return stats.toList()
    }

    fun applyEffects(time: Int) {
        effects.toList().forEach {
            it.applyEffect(this, time)
        }
    }


    fun onFirstApply(condition: Condition, soul: Soul) {
        soul.conditions.toList().forEach { onFirstConditionApply(condition, it) }
    }

    private fun onFirstConditionApply(newCondition: Condition, existingCondition: Condition) {
        val interaction = newCondition.element.getReaction(newCondition.elementStrength, existingCondition.element, existingCondition.elementStrength)

        when (interaction){
            ElementInteraction.STRONGER -> null //Clear existing condition
            ElementInteraction.WEAKER -> null // Prevent new condition from being applied
            ElementInteraction.CRITICAL -> null // Apply critical bonus to new condition
            ElementInteraction.REVERSE_CRITICAL -> null //Apply critical bonus to old condition
            else -> return
        }

    }

}