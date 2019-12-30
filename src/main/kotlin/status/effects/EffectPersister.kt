package status.effects

import status.conditions.Condition

fun getPersisted(dataObject: Effect): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    return data
}

fun readFromData(data: Map<String, Any>): Effect {
    return Condition("NONE")
}