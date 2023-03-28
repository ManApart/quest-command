package inventory.pickupItem

import core.events.Event
import core.thing.Thing

data class ItemPickedUpEvent(val source: Thing, val item: Thing, val silent: Boolean = false) : Event