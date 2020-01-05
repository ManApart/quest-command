package traveling.location

import core.body.BodyManager
import inventory.Inventory
import traveling.location.location.LocationManager

fun getPersisted(dataObject: Network): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["locations"] = dataObject.getLocations().map { traveling.location.location.getPersisted(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, inventory: Inventory?): Network {
    val name = data["name"] as String
    val network = if (LocationManager.networkExists(name)) {
        LocationManager.getNetwork(name)
    } else {
        BodyManager.getBody(name).layout
    }
    (data["locations"] as List<Map<String, Any>>).map { traveling.location.location.applyFromData(it, network, inventory) }

    return network
}