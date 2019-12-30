package status

import status.conditions.getPersisted
import status.stat.getPersisted

fun getPersisted(dataObject: Soul): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["stats"] = dataObject.getStats().map { getPersisted(it) }
    data["conditions"] = dataObject.getConditions().map { getPersisted(it) }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, soul: Soul) {
    val stats = (data["stats"] as List<Map<String, Any>>).map { status.stat.readFromData(it, soul.parent) }
    val conditions = (data["conditions"] as List<Map<String, Any>>).map { status.conditions.readFromData(it, soul.parent) }

    soul.overrideStats(stats)
    soul.overrideConditions(conditions)
}