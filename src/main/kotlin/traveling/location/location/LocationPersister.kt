package traveling.location.location

import core.body.getPersisted
import core.body.readBodyPartFromData
import core.properties.getPersisted

fun getPersisted(dataObject: Location): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["description"] = dataObject.description
    data["activators"] = dataObject.activators.map { getPersisted(it) }
    data["creatures"] = dataObject.creatures.map { getPersisted(it) }
    data["items"] = dataObject.items.map { getPersisted(it) }
    data["properties"] = getPersisted(dataObject.properties)
    if (dataObject.bodyPart != null) {
        data["bodyPart"] = getPersisted(dataObject.bodyPart)
    }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Location {
    val name = data["name"] as String
    val description = data["description"] as String
    val activators = (data["activators"] as List<Map<String, Any>>).map { readLocationTargetFromData(it) }
    val creatures = (data["creatures"] as List<Map<String, Any>>).map { readLocationTargetFromData(it) }
    val items = (data["items"] as List<Map<String, Any>>).map { readLocationTargetFromData(it) }
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)
    val bodyPart = if (data["bodyPart"] == null) {
        null
    } else {
        readBodyPartFromData(data["bodyPart"] as Map<String, Any>)
    }

    return Location(name, description, activators, creatures, items, bodyPart, properties)
}