package core.gameState

import status.StatMaxedEvent
import status.StatMinnedEvent
import system.EventManager

class Soul(private val stats: MutableList<Stat> = mutableListOf()) {

    fun incStat(creature: Creature, type: String, amount: Int) {
        if (amount != 0 && getStatOrNull(type) != null) {
            val stat = getStatOrNull(type)!!
            stat.current += amount
            stat.current = Math.max(Math.min(stat.current, stat.max), 0)

            if (stat.current == 0) {
                EventManager.postEvent(StatMinnedEvent(creature, stat.type))
            } else if (stat.current == stat.max) {
                EventManager.postEvent(StatMaxedEvent(creature, stat.type))
            }
        }
    }

    fun addStats(stats: Map<String, String>) {
        stats.forEach { addStat(it.key, Integer.parseInt(it.value)) }
    }

    fun addStat(type: String, max: Int) {
        stats.add(Stat(type, max))
    }

    fun hasStat(type: String): Boolean {
        return getStatOrNull(type) != null
    }

    fun getCurrent(type: String): Int {
        return getStatOrNull(type)?.current ?: 0
    }

    fun getTotal(type: String): Int {
        return getStatOrNull(type)?.max ?: 0
    }

    private fun getStatOrNull(type: String): Stat? {
        return stats.firstOrNull { it.type.toLowerCase() == type.toLowerCase() }
    }


}