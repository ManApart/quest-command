package status.statChanged

import core.events.Event
import core.gameState.Target

class StatMinnedEvent(val target: Target, val stat: String) : Event