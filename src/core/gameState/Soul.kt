package core.gameState

import status.StatMaxedEvent
import status.StatMinnedEvent
import system.EventManager

class Soul {
    private val stats = mutableListOf(
            Stat(Stat.HEALTH, 10),
            Stat(Stat.STAMINA, 10))

    fun incStat(type: String, amount: Int){
        if (amount != 0) {
            val stat = getStat(type)
            stat.current += amount
            stat.current = Math.max(Math.min(stat.current, stat.max), 0)

            val change = if (amount > 0) "increases" else "decreases"
            println("Your ${stat.type} $change to ${stat.current}/${stat.max}")

            if (stat.current == 0) {
                EventManager.postEvent(StatMinnedEvent(stat.type))
            } else if (stat.current == stat.max) {
                EventManager.postEvent(StatMaxedEvent(stat.type))
            }
        }
    }

    fun addStat(type: String, max: Int){
        stats.add(Stat(type, max))
    }

    fun hasStat(type: String) : Boolean {
        return getStatOrNull(type) != null
    }

    fun getCurrent(type: String) : Int {
        return getStat(type).current
    }

    fun getTotal(type: String) : Int {
        return getStat(type).max
    }

    private fun getStat(type: String) : Stat {
        return getStatOrNull(type)!!
    }

    private fun getStatOrNull(type: String) : Stat? {
        return stats.firstOrNull{ it.type.toLowerCase() == type.toLowerCase()}
    }
}