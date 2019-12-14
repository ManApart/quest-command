package inventory.dropItem

import core.events.Event
import core.target.Target

class ItemDroppedEvent(val source: Target, val item: Target, val silent: Boolean = false) : Event