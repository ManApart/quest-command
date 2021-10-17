package status.statChanged

import core.events.Event
import core.thing.Thing

class StatMinnedEvent(val thing: Thing, val stat: String) : Event