package status

import core.events.Event
import core.target.Target
import status.stat.LeveledStat

class LevelUpEvent(val source: Target, val leveledStat: LeveledStat, val level: Int) : Event