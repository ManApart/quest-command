package inventory.equipItem

import core.body.Slot
import core.events.Event
import core.thing.Thing

class EquipItemEvent(val creature: Thing, val item: Thing, val slot: Slot? = null) : Event