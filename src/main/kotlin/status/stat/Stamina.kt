package status.stat

import core.thing.Thing
import traveling.position.Distances
import traveling.position.Vector

//TODO - add encumbrance cost

fun getStaminaCost(movementAmount: Vector, staminaScalar: Float = 1f): Int {
    return getStaminaCost(movementAmount.getDistance(), staminaScalar)
}

fun getStaminaCost(movementAmount: Int, staminaScalar: Float = 1f): Int {
    return (movementAmount / distancePerStamina(staminaScalar)).toInt()
}

fun Thing.getMaxPossibleMovement(staminaScalar: Float = 1f): Int {
    return (soul.getCurrent(STAMINA) * distancePerStamina(staminaScalar)).toInt()
}

private fun distancePerStamina(staminaScalar: Float): Float {
    return Distances.HUMAN_LENGTH / staminaScalar
}