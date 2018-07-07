package status

import core.events.Event
import core.gameState.Creature
import core.gameState.Stat

class StatChangeEvent(val target: Creature, val source: String, val type: Stat.StatType, val amount: Int) : Event