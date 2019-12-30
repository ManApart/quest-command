package core.target

import core.properties.getPersisted


fun getPersisted(dataObject: Target): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data["name"] = dataObject.name
    data["aiName"] = dataObject.ai.name
    data["properties"] = getPersisted(dataObject.properties)
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Target {
    val name = data["name"] as String
    val aiName = data["aiName"] as String
    val props = core.properties.readFromData(data["properties"] as Map<String, Any>)
    return Target(name, aiName = aiName, properties = props)
}
