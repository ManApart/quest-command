package traveling.move

import core.events.TemporalEvent
import core.thing.Thing
import status.stat.AGILITY
import traveling.position.Vector
import kotlin.math.max

data class MoveEvent(override val creature: Thing, val sourcePosition: Vector = creature.position, val destination: Vector, val staminaScalar: Float = 1f, val silent: Boolean = false, override var timeLeft: Int = 1) : TemporalEvent

suspend fun startMoveEvent(
    source: Thing,
    destination: Vector,
    staminaScalar: Float = 1f,
    speedScalar: Float = 1f,
    silent: Boolean = false,
    timeLeft: Int? = null
): MoveEvent {
    return MoveEvent(source, source.position, destination, staminaScalar, silent, timeLeft ?: calcTimeLeft(source, destination, speedScalar))
}

private suspend fun calcTimeLeft(source: Thing, moveThing: Vector, speedScalar: Float): Int {
    val agility = getUnencumberedAgility(source)
    val distance = source.position.getDistance(moveThing)
    if (distance == 0) return 1
    val speed = max(agility - distance, 1)

    return max(1, 100 / (speedScalar * speed).toInt())
}

private suspend fun getUnencumberedAgility(thing: Thing): Int {
    val agility = thing.soul.getCurrent(AGILITY)
    val encumbrance = thing.getEncumbranceInverted()
    return (agility * encumbrance).toInt()
}
