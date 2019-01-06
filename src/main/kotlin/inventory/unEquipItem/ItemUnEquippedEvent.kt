package inventory.unEquipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item
import core.gameState.bodies.Slot

class ItemUnEquippedEvent(val creature: Creature, val item: Item, val slot: Slot) : Event