package inventory.putItem

import core.events.TemporalEvent
import core.thing.Thing

data class TransferItemEvent(
    override val creature: Thing,
    val item: Thing,
    val source: Thing,
    val destination: Thing,
    val count: Int = 1,
    val silent: Boolean = false,
    override var timeLeft: Int = if(creature.isPlayer()) 1 else 0
) : TemporalEvent