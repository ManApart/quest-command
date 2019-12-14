package status.statChanged

import core.events.Event
import core.target.Target

class StatMinnedEvent(val target: Target, val stat: String) : Event