package system.message

import core.events.Event
import core.target.Target

class DisplayMessageEvent(val listener: Target, val message: String) : Event