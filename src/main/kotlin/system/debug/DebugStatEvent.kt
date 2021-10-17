package system.debug

import core.events.Event
import core.thing.Thing
import status.stat.StatKind

class DebugStatEvent(val thing: Thing, val statKind: StatKind, val statName: String, val level: Int) : Event