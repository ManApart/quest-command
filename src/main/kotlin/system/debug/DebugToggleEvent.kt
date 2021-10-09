package system.debug

import core.events.Event
import core.target.Target

class DebugToggleEvent(val source: Target, val debugType: DebugType, val toggledOn: Boolean) : Event