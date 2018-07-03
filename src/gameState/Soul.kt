package gameState

import events.StatMaxedEvent
import events.StatMinnedEvent

class Soul {
    private val stats = listOf(
            Stat(Stat.StatType.HEALTH, 10),
            Stat(Stat.StatType.STAMINA, 10))

    fun incStat(type: Stat.StatType, amount: Int){
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

    fun getCurrent(type: Stat.StatType) : Int {
        return getStat(type).current
    }

    fun getTotal(type: Stat.StatType) : Int {
        return getStat(type).max
    }

    private fun getStat(type: Stat.StatType) : Stat {
        return stats.first{ it.type == type}
    }
}