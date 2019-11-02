package status.propValChanged

import core.events.Event
import core.gameState.Target

class PropertyStatMinnedEvent(val target: Target, val stat: String) : Event