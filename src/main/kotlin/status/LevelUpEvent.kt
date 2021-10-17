package status

import core.events.Event
import core.thing.Thing
import status.stat.LeveledStat

class LevelUpEvent(val source: Thing, val leveledStat: LeveledStat, val level: Int) : Event