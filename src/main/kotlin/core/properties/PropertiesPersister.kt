package core.properties

import system.persistance.getListKey
import system.persistance.getMapKey


fun getPersisted(dataObject: Properties): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data["version"] = 1
    data["values"] = dataObject.values.getAll()
    data["tags"] = dataObject.tags.getAll()
    return data
}

fun readFromData(data: Map<String, Any>): Properties {
    val values = Values()
    getMapKey(data, "values").forEach { values.put(it.key, it.value as String) }

    val tags = Tags()
    getListKey(data, "tags").forEach { tags.add(it) }

    return Properties(values, tags)
}