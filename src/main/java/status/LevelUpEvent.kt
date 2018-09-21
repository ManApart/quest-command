package status

import core.events.Event
import core.gameState.Creature
import core.gameState.stat.Stat

class LevelUpEvent(val creature: Creature, val stat: Stat, val level: Int) : Event