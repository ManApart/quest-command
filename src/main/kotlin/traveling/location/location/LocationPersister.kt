package traveling.location.location

import core.properties.getPersisted
import inventory.Inventory
import traveling.location.Network

fun getPersisted(dataObject: Location): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["properties"] = getPersisted(dataObject.properties)
//TODO - persist equipped items etc
    return data
}

@Suppress("UNCHECKED_CAST")
fun applyFromData(data: Map<String, Any>, network: Network, inventory: Inventory?) {
    val name = data["name"] as String
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)
    val location = network.getLocation(name)
    location.properties.replaceWith(properties)
}