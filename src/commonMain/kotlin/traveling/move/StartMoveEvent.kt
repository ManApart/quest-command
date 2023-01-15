package traveling.move

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing
import status.stat.AGILITY
import traveling.position.Vector
import kotlin.math.max

suspend fun startMoveEvent(
    source: Thing,
    moveThing: Vector,
    staminaScalar: Float = 1f,
    speedScalar: Float = 1f,
    silent: Boolean = false,
    timeLeft: Int? = null
): StartMoveEvent {
    return StartMoveEvent(source, moveThing, staminaScalar, silent, timeLeft ?: calcTimeLeft(source, moveThing, speedScalar))
}

class StartMoveEvent(
    override val source: Thing,
    private val moveThing: Vector,
    private val staminaScalar: Float = 1f,
    private val silent: Boolean = false,
    override var timeLeft: Int
) : Event, DelayedEvent {

    override fun getActionEvent(): MoveEvent {
        return MoveEvent(source, source.position, moveThing, staminaScalar, silent)
    }
}

private suspend fun calcTimeLeft(source: Thing, moveThing: Vector, speedScalar: Float): Int {
    val agility = getUnencumberedAgility(source)
    val distance = source.position.getDistance(moveThing)
    val speed = max(agility - distance, 1)

    return max(1, 100 / (speedScalar * speed).toInt())
}

private suspend fun getUnencumberedAgility(thing: Thing): Int {
    val agility = thing.soul.getCurrent(AGILITY)
    val encumbrance = thing.getEncumbranceInverted()
    return (agility * encumbrance).toInt()
}