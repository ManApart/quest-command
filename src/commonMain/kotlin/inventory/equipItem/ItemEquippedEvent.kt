package inventory.equipItem

import core.body.EquipTarget
import core.body.Slot
import core.events.TemporalEvent
import core.thing.Thing

data class ItemEquippedEvent(override val creature: Thing, val item: Thing, val target: EquipTarget, override var timeLeft: Int = 1) : TemporalEvent
