package inventory.dropItem

import core.events.Event
import core.gameState.Item
import core.gameState.Target

class ItemDroppedEvent(val source: Target, val item: Item, val silent: Boolean = false) : Event