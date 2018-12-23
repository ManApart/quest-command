package status

import core.events.Event
import core.gameState.Target
import core.gameState.stat.Stat

class LevelUpEvent(val source: Target, val stat: Stat, val level: Int) : Event