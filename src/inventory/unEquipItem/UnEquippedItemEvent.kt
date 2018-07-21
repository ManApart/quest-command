package inventory.unEquipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class UnEquippedItemEvent(val source: Creature, val item: Item) : Event