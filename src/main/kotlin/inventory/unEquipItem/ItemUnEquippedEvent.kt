package inventory.unEquipItem

import core.body.Slot
import core.events.Event
import core.target.Target

class ItemUnEquippedEvent(val creature: Target, val item: Target, val slot: Slot) : Event