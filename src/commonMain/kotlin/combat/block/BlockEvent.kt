package combat.block

import core.events.TemporalEvent
import core.thing.Thing
import status.stat.AGILITY
import traveling.location.location.Location
import kotlin.math.max


class BlockEvent(
    override val creature: Thing,
    val partThatWillShield: Location,
    val partThatWillBeShielded: Location,
    override var timeLeft: Int = if (creature.isPlayer()) 1 else 0
) : TemporalEvent

suspend fun startBlockEvent(source: Thing, partThatWillShield: Location, partThatWillBeShielded: Location, timeLeft: Int? = null): BlockEvent {
    return BlockEvent(source, partThatWillShield, partThatWillBeShielded, timeLeft ?: calcTimeLeft(source))
}

private suspend fun calcTimeLeft(source: Thing): Int {
    //TODO - better calculation for block, take into account the shield etc
    val encumbrance = source.getEncumbrance()
    val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())

    return max(1, 100 / agility)
}
