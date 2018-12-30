package core.gameState

import core.gameState.stat.Stat

class Soul(val parent: Target, private val stats: MutableList<Stat> = mutableListOf()) {
    var effects = mutableListOf<Effect>()

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

    fun getCurrent(name: String): Int {
        return getStatOrNull(name)?.current ?: 0
    }

    fun getTotal(name: String): Int {
        return getStatOrNull(name)?.max ?: 0
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

    private fun getEffectOrNull(name: String): Effect? {
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

}