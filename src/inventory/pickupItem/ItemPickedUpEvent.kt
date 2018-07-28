package inventory.pickupItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class ItemPickedUpEvent(val source: Creature, val item: Item) : Event