package inventory.pickupItem

import core.events.Event
import core.target.Target

class ItemPickedUpEvent(val source: Target, val item: Target, val silent: Boolean = false) : Event