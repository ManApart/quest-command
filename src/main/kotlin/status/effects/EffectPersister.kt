package status.effects

import core.body.Body


fun getPersisted(dataObject: Effect): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["amount"] = dataObject.amount
    data["duration"] = dataObject.duration
    data["originalValue"] = dataObject.originalValue
    data["bodyParts"] = dataObject.bodyPartThings.map { it.name }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Effect {
    val bodyParts = (data["bodyParts"] as List<String>).map { body.getPart(it) }
    return EffectManager.getEffect(
            data["name"] as String,
            data["amount"] as Int,
            data["duration"] as Int,
            bodyParts
    )
}