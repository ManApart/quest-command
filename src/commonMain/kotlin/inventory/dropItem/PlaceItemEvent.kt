package inventory.dropItem

import core.events.TemporalEvent
import core.thing.Thing
import traveling.position.Vector

data class PlaceItemEvent(
    override val creature: Thing,
    val item: Thing,
    val position: Vector = creature.position,
    val count: Int = 1,
    val silent: Boolean = false,
    override var timeLeft: Int = if(creature.isPlayer()) 1 else 0
) : TemporalEvent