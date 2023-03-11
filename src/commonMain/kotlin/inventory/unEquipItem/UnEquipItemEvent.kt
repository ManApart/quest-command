package inventory.unEquipItem

import core.events.TemporalEvent
import core.thing.Thing

class UnEquipItemEvent(override val creature: Thing, val item: Thing, override var timeLeft: Int = 1) : TemporalEvent