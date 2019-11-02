package status

import core.events.Event
import core.gameState.Target
import core.gameState.stat.LeveledStat

class LevelUpEvent(val source: Target, val leveledStat: LeveledStat, val level: Int) : Event