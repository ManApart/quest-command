package inventory.equipItem

import core.events.Event
import core.target.Target
import core.body.Slot

class EquipItemEvent(val creature: Target, val item: Target, val slot: Slot? = null) : Event