package core.body

import inventory.Inventory

fun getPersisted(dataObject: Body): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["layout"] = traveling.location.getPersisted(dataObject.layout)
//    data["parts"] = dataObject.layout.getLocations().map { traveling.location.location.getPersisted(it) }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, inventory: Inventory?): Body {
    val name = data["name"] as String
    val network = traveling.location.readFromData(data["layout"] as Map<String, Any>, inventory)
    return  Body(name, network)
}