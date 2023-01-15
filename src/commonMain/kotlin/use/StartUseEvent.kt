package use

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing
import status.stat.AGILITY
import kotlin.math.max

suspend fun startUseEvent(source: Thing, used: Thing, thing: Thing, timeLeft: Int? = null): StartUseEvent {
    return StartUseEvent(source, used, thing, timeLeft ?: calcTimeLeft(source, used))
}

class StartUseEvent(override val source: Thing, val used: Thing, val thing: Thing, override var timeLeft: Int) : Event, DelayedEvent {
    override fun getActionEvent(): UseEvent {
        return UseEvent(source, used, thing)
    }
}

private suspend fun calcTimeLeft(source: Thing, used: Thing): Int {
    val weaponSize = used.properties.getRange()
    val weaponWeight = used.getWeight()
    val encumbrance = source.getEncumbrance()
    val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())
    val weaponScaleFactor = 10

    return max(1, weaponSize * weaponWeight * weaponScaleFactor / agility)
}