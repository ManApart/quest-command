package status

import core.body.Body
import status.conditions.getPersisted
import status.stat.getPersisted

fun getPersisted(dataObject: Soul): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["stats"] = dataObject.getStats().map { getPersisted(it) }
    data["conditions"] = dataObject.getConditions().map { getPersisted(it) }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Soul {
    val stats = (data["stats"] as List<Map<String, Any>>).map { status.stat.readFromData(it) }.toMutableList()
    val conditions = (data["conditions"] as List<Map<String, Any>>).map { status.conditions.readFromData(it, body) }

    val soul = Soul(stats)
    soul.overrideConditions(conditions)
    return soul
}