package status.statChanged

import core.events.Event
import core.gameState.Target

class StatMaxedEvent(val target: Target, val stat: String) : Event