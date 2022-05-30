package status.statChanged

import core.events.Event
import core.thing.Thing

class StatMaxedEvent(val thing: Thing, val stat: String) : Event