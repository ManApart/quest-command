package system.debug

import core.events.Event
import core.thing.Thing

class DebugToggleEvent(val source: Thing, val debugType: DebugType, val toggledOn: Boolean) : Event