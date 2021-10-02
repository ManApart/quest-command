package system.debug

import core.events.Event
import core.target.Target

class DebugTagEvent(val target: Target, val tag: String, val isAddingTag: Boolean) : Event