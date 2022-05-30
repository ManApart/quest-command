package inventory.pickupItem

import core.events.Event
import core.thing.Thing

class ItemPickedUpEvent(val source: Thing, val item: Thing, val silent: Boolean = false) : Event