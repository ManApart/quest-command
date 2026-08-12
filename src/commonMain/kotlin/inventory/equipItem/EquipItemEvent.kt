package inventory.equipItem

import core.body.EquipTarget
import core.body.Slot
import core.events.Event
import core.thing.Thing

data class EquipItemEvent(val creature: Thing, val item: Thing, val target: EquipTarget? = null) : Event
