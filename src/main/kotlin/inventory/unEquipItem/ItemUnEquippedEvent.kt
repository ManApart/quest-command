package inventory.unEquipItem

import core.events.Event
import core.gameState.Target
import core.gameState.body.Slot

class ItemUnEquippedEvent(val creature: Target, val item: Target, val slot: Slot) : Event