package core.properties

import core.GameState
import quests.QuestManager
import quests.QuestP
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

@kotlinx.serialization.Serializable
data class PropertiesP(
    val tags: List<String>,
    val values: Map<String, String>
) {
    constructor(b: Properties) : this(b.tags.getAll(), b.values.getAll())

    fun parsed(): Properties {
        val props = Properties()
        props.tags.addAll(tags)
        values.entries.forEach { (key, value) ->
            props.values.put(key, value)
        }
        return props
    }
}