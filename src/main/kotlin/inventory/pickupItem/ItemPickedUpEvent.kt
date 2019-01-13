package inventory.pickupItem

import core.events.Event
import core.gameState.Item
import core.gameState.Target

class ItemPickedUpEvent(val source: Target, val item: Item, val silent: Boolean = false) : Event