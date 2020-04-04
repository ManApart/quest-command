package traveling.location

import core.body.BodyManager
import inventory.Inventory
import system.persistance.clean
import system.persistance.getFiles
import system.persistance.loadMaps
import traveling.location.location.LocationManager

fun persist(dataObject: Network, path: String) {
    val path = path + clean(dataObject.name) + "/" + dataObject.name

    dataObject.getLocationNodes()
            .filter { it.hasLoadedLocation() }
            .map { it.getLocation() }
            .map { traveling.location.location.persist(it, path) }
}

@Suppress("UNCHECKED_CAST")
fun load(path: String, name: String): Network {
    val network = if (LocationManager.networkExists(name)) {
        LocationManager.getNetwork(name)
    } else {
        BodyManager.getBody(name).layout
    }
    getFiles(path).forEach {
        network.getLocationNode(it.nameWithoutExtension).loadPath = it.path
    }

    return network
}