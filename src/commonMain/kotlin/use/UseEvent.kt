package use

import core.events.TemporalEvent
import core.thing.Thing
import status.stat.AGILITY
import kotlin.math.max

/**
 * A source uses an item on a thing. Different from Interact in that there is something being used ON/WITH something else
 */
class UseEvent(override val source: Thing, val used: Thing, val usedOn: Thing, override var timeLeft: Int = 1) : TemporalEvent {
    override fun gameTicks(): Int = 1
}

suspend fun startUseEvent(source: Thing, used: Thing, thing: Thing, timeLeft: Int? = null): UseEvent {
    return UseEvent(source, used, thing, timeLeft ?: calcTimeLeft(source, used))
}

private suspend fun calcTimeLeft(source: Thing, used: Thing): Int {
    val weaponSize = used.properties.getRange()
    val weaponWeight = used.getWeight()
    val encumbrance = source.getEncumbrance()
    val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())
    val weaponScaleFactor = 10

    return max(1, weaponSize * weaponWeight * weaponScaleFactor / agility)
}