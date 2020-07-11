package traveling.move

import core.events.DelayedEvent
import core.events.Event
import core.target.Target
import status.stat.AGILITY
import traveling.position.Vector
import kotlin.math.max

class StartMoveEvent(override val source: Target, private val moveTarget: Vector, private val staminaScalar: Float = 1f, private val speedScalar: Float = 1f, private val silent: Boolean = false, timeLeft: Int = -1) : Event, DelayedEvent {
    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1) {
            defaultTimeLeft
        } else {
            val agility = getUnencumberedAgility(source)
            val distance = source.position.getDistance(moveTarget)
            val speed = max(agility - distance, 1)

            max(1, 100 / (speedScalar * speed).toInt())
        }
    }

    private fun getUnencumberedAgility(target: Target): Int {
        val agility = target.soul.getCurrent(AGILITY)
        val encumbrance = target.getEncumbranceInverted()
        return (agility * encumbrance).toInt()
    }


    override fun getActionEvent(): MoveEvent {
        return MoveEvent(source, source.position, moveTarget, staminaScalar, silent)
    }
}