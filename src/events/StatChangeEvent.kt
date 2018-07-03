package events

import gameState.Stat

class StatChangeEvent(val source: String, val type: Stat.StatType, val amount: Int) : Event