package system.message

import core.events.Event
import core.target.Target

class MessageEvent(val source: Target, val message: String) : Event