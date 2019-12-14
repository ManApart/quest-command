package inventory.unEquipItem

import core.events.Event
import core.target.Target
import core.body.Slot

class ItemUnEquippedEvent(val creature: Target, val item: Target, val slot: Slot) : Event