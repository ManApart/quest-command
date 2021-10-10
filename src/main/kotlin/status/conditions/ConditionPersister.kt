package status.conditions

import core.body.Body
import magic.Element
import status.effects.getPersisted

fun getPersisted(dataObject: Condition): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["element"] = dataObject.element.name
    data["elementStrength"] = dataObject.elementStrength
    data["age"] = dataObject.age
    data["permanent"] = dataObject.permanent
    data["isCritical"] = dataObject.isCritical
    data["isFirstApply"] = dataObject.isFirstApply
    data["effects"] = dataObject.effects.map { getPersisted(it) }
    data["criticalEffects"] = dataObject.criticalEffects.map { getPersisted(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Condition {

    val effects = (data["effects"] as List<Map<String, Any>>).map { status.effects.readFromData(it, body) }
    val criticalEffects = (data["criticalEffects"] as List<Map<String, Any>>).map { status.effects.readFromData(it, body) }
    return Condition(
            data["name"] as String,
            Element.valueOf(data["element"] as String),
            data["elementStrength"] as Int,
            effects,
            criticalEffects,
            data["permanent"] as Boolean,
            data["age"] as Int,
            data["isCritical"] as Boolean,
            data["isFirstApply"] as Boolean
    )
}