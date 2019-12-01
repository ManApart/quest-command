package system.debug

import core.events.Event

class DebugToggleEvent(val debugType: DebugType, val toggledOn: Boolean) : Event