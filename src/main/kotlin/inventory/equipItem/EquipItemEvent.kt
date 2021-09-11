package inventory.equipItem

import core.body.Slot
import core.events.Event
import core.target.Target

class EquipItemEvent(val creature: Target, val item: Target, val slot: Slot? = null) : Event