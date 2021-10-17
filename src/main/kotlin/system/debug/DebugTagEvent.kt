package system.debug

import core.events.Event
import core.thing.Thing

class DebugTagEvent(val thing: Thing, val tag: String, val isAddingTag: Boolean) : Event