package system.debug

import core.events.Event
import core.gameState.Target

class DebugToggleEvent(val debugType: DebugType, val toggledOn: Boolean) : Event