package traveling.location.location

import core.body.getPersisted
import core.body.readBodyPartFromData
import core.properties.getPersisted
import inventory.Inventory
import traveling.location.Network

fun getPersisted(dataObject: Location): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["properties"] = getPersisted(dataObject.properties)
    if (dataObject.bodyPart != null) {
        data["bodyPart"] = getPersisted(dataObject.bodyPart!!)
    }

    return data
}

@Suppress("UNCHECKED_CAST")
fun applyFromData(data: Map<String, Any>, network: Network, inventory: Inventory?) {
    val name = data["name"] as String
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)
    val bodyPart = if (data["bodyPart"] == null) {
        null
    } else {
        readBodyPartFromData(data["bodyPart"] as Map<String, Any>, inventory)
    }
    val location = network.getLocation(name)
    location.bodyPart = bodyPart
    location.properties.replaceWith(properties)
}