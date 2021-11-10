package status.stat

import core.thing.Thing
import traveling.position.Distances
import traveling.position.Vector
import kotlin.math.roundToInt

//TODO - add encumbrance cost

fun getStaminaCost(movementAmount: Vector, staminaScalar: Float = 1f): Int {
    return getStaminaCost(movementAmount.getDistance(), staminaScalar)
}

fun getStaminaCost(movementAmount: Int, staminaScalar: Float = 1f): Int {
    return (movementAmount * staminaScalar).roundToInt() / Distances.HUMAN_LENGTH
}

fun Thing.getMaxPossibleMovement(staminaScalar: Float = 1f): Int {
    return (soul.getCurrent(STAMINA) / staminaScalar).toInt() * Distances.HUMAN_LENGTH
}