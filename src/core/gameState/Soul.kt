package core.gameState

import core.gameState.stat.Stat
import status.statChanged.StatMaxedEvent
import status.statChanged.StatMinnedEvent
import system.EventManager

class Soul(val creature: Creature, private val stats: MutableList<Stat> = mutableListOf()) {
    var effects = mutableListOf<Effect>()

    fun incStat(creature: Creature, name: String, amount: Int) {
        if (amount != 0) {
            if (getStatOrNull(name) == null) {
                addStat(name)
            }
            val stat = getStatOrNull(name)!!
            incStat(creature, stat, amount)
        }
    }

    fun incStat(creature: Creature, stat: Stat, amount: Int) {
        if (amount != 0) {
            stat.current += amount
            stat.current = Math.max(Math.min(stat.current, stat.baseMax), 0)

            if (stat.current == 0) {
                EventManager.postEvent(StatMinnedEvent(creature, stat.name))
            } else if (stat.current == stat.baseMax) {
                EventManager.postEvent(StatMaxedEvent(creature, stat.name))
            }
        }
    }

    fun addStats(stats: Map<String, String>) {
        stats.forEach { addStat(it.key, Integer.parseInt(it.value)) }
    }

    fun addStat(name: String, level: Int = 1, maxMultiplier: Int = 1, expExponential: Int = 2) {
        stats.add(Stat(name, level, maxMultiplier, expExponential))
    }

    fun hasStat(name: String): Boolean {
        return getStatOrNull(name) != null
    }

    fun getCurrent(name: String): Int {
        return getStatOrNull(name)?.current ?: 0
    }

    fun getTotal(name: String): Int {
        return getStatOrNull(name)?.boostedMax ?: 0
    }

    fun getStatOrNull(name: String): Stat? {
        return stats.firstOrNull { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getStats() : List<Stat> {
        return stats.toList()
    }

    fun applyEffects(){
        effects.forEach {
            it.applyEffect(this)
        }
    }

}