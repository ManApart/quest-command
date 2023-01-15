package use

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing
import status.stat.AGILITY
import kotlin.math.max

class StartUseEvent(override val source: Thing, val used: Thing, val thing: Thing, timeLeft: Int = -1) : Event, DelayedEvent {
    override var timeLeft = calcTimeLeft(timeLeft)

    private suspend fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            val weaponSize = used.properties.getRange()
            val weaponWeight = used.getWeight()
            val encumbrance = source.getEncumbrance()
            val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())
            val weaponScaleFactor = 10

            max(1, weaponSize * weaponWeight * weaponScaleFactor / agility)
        }
    }

    override fun getActionEvent(): UseEvent {
        return UseEvent(source, used, thing)
    }
}