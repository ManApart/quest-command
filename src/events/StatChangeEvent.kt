package events

import gameState.Creature
import gameState.Stat

class StatChangeEvent(val target: Creature, val source: String, val type: Stat.StatType, val amount: Int) : Event