package inventory.unEquipItem

import core.body.Slot
import core.events.Event
import core.thing.Thing

class ItemUnEquippedEvent(val creature: Thing, val item: Thing, val slot: Slot) : Event