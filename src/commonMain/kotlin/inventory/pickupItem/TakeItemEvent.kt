package inventory.pickupItem

import core.events.TemporalEvent
import core.thing.Thing

data class TakeItemEvent(
    override val creature: Thing,
    val item: Thing,
    val count: Int = 1,
    val silent: Boolean = false,
    override var timeLeft: Int = if(creature.isPlayer()) 1 else 0
) : TemporalEvent