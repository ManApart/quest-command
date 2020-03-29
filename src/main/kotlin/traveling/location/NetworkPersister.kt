package traveling.location

import core.body.BodyManager
import inventory.Inventory
import system.persistance.clean
import traveling.location.location.LocationManager

fun persist(dataObject: Network, path: String) {
    val path = path + clean(dataObject.name) + "/" + dataObject.name

    dataObject.getLocationNodes()
            .filter { it.hasLoadedLocation() }
            .map { it.getLocation() }
            .map { traveling.location.location.persist(it, path) }
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, inventory: Inventory?): Network {
    val name = data["name"] as String
    val network = if (LocationManager.networkExists(name)) {
        LocationManager.getNetwork(name)
    } else {
        BodyManager.getBody(name).layout
    }
//    (data["locations"] as List<Map<String, Any>>).map { traveling.location.location.applyFromData(it, network, inventory) }

    return network
}