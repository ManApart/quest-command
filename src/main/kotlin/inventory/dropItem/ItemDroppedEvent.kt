package inventory.dropItem

import core.events.Event
import core.thing.Thing

class ItemDroppedEvent(val source: Thing, val item: Thing, val silent: Boolean = false) : Event