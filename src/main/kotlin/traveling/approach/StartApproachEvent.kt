package traveling.approach

import core.GameState
import core.events.DelayedEvent
import core.events.Event
import core.target.Target
import status.stat.AGILITY
import traveling.approach.ApproachEvent
import traveling.direction.Vector
import kotlin.math.max

class StartApproachEvent(override val source: Target, private val moveTarget: Vector, timeLeft: Int = -1) : Event, DelayedEvent {
    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1) {
            defaultTimeLeft
        } else {
            val agility = getUnencumberedAgility(source)
            val distance = GameState.battle?.getCombatantDistance() ?: 0
            val speed = max(agility - distance, 1)

            max(1, 100 / speed)
        }
    }

    private fun getUnencumberedAgility(target: Target): Int {
        val agility = target.soul.getCurrent(AGILITY)
        val encumbrance = target.getEncumbranceInverted()
        return (agility * encumbrance).toInt()
    }

    override fun getActionEvent(): ApproachEvent {
        return ApproachEvent(source, moveTarget)
    }
}
