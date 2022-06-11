package system.message

import core.events.Event
import core.thing.Thing

class DisplayMessageEvent(val listener: Thing, val message: String) : Event