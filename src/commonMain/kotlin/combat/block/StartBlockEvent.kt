package combat.block

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing
import status.stat.AGILITY
import traveling.location.location.Location
import kotlin.math.max

suspend fun startBlockEvent(source: Thing, partThatWillShield: Location, partThatWillBeShielded: Location,timeLeft: Int? = null): StartBlockEvent{
    return StartBlockEvent(source, partThatWillShield, partThatWillBeShielded, timeLeft ?: calcTimeLeft(source))
}

class StartBlockEvent(override val source: Thing, private val partThatWillShield: Location, private val partThatWillBeShielded: Location, override var timeLeft: Int) : Event, DelayedEvent {

    override fun getActionEvent(): BlockEvent {
        return BlockEvent(source, partThatWillShield, partThatWillBeShielded)
    }
}

private suspend fun calcTimeLeft(source: Thing): Int {
    //TODO - better calculation for block, take into account the shield etc
    val encumbrance = source.getEncumbrance()
    val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())

    return max(1, 100 / agility)
}
