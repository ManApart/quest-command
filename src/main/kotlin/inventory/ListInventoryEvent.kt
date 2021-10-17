package inventory

import core.events.Event
import core.thing.Thing

class ListInventoryEvent(val thing: Thing) : Event