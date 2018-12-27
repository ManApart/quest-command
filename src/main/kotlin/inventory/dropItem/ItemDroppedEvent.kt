package inventory.dropItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class ItemDroppedEvent(val source: Creature, val item: Item, val silent: Boolean = false) : Event