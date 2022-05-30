package traveling.location

import core.body.BodyManager
import core.thing.Thing
import system.persistance.clean
import system.persistance.getFiles
import traveling.location.location.LocationManager

//TODO - save discovered
fun persist(dataObject: Network, path: String, ignoredThings: List<Thing>) {
    val cleanedPath = clean(path, dataObject.name)

    dataObject.getLocationNodes()
            .filter { it.hasLoadedLocation() }
            .map { it.getLocation() }
            .map { traveling.location.location.persist(it, cleanedPath, ignoredThings) }
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