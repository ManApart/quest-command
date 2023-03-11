package inventory.equipItem

import core.body.Slot
import core.events.TemporalEvent
import core.thing.Thing

class ItemEquippedEvent(override val creature: Thing, val item: Thing, val slot: Slot, override var timeLeft: Int = 1) : TemporalEvent