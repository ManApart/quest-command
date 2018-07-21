package inventory.equipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item
import core.gameState.Slot

class EquippedItemEvent(val creature: Creature, val item: Item, val slot: Slot) : Event