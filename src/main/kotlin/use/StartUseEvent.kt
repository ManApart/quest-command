package use

import core.events.DelayedEvent
import core.events.Event
import core.target.Target
import status.stat.AGILITY
import kotlin.math.max

class StartUseEvent(override val source: Target, val used: Target, val target: Target, timeLeft: Int = -1) : Event, DelayedEvent {
    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
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
        return UseEvent(source, used, target)
    }
}