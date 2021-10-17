package traveling.location.location

import core.properties.getPersisted
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import system.persistance.*
import traveling.location.network.LocationNode

fun persist(dataObject: Location, path: String, ignoredThings: List<Thing> = listOf()) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile(".json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.locationNode.name
    data["properties"] = getPersisted(dataObject.properties)
    writeSave(path, saveName, data)

    data["activators"] = dataObject.getActivators().map { core.thing.persistToDisk(it, clean(prefix, "activators")) }
    data["creatures"] = dataObject.getCreatures().filter { !it.isPlayer() }.map { core.thing.persistToDisk(it, clean(prefix, "creatures")) }
    data["items"] = dataObject.getItems().filter { !ignoredThings.contains(it) }.map { core.thing.persistToDisk(it, clean(prefix, "items")) }
    data["other"] = dataObject.getOther().map { core.thing.persistToDisk(it, clean(prefix, "other")) }
    //Persist weather
    //Persist last weather change

}

@Suppress("UNCHECKED_CAST")
fun load(path: String, locationNode: LocationNode): Location {
    val data = loadMap(path)
    val folderPath = path.removeSuffix(".json")
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val activators = getThings(folderPath, "activators", locationNode)
    val creatures = getThings(folderPath, "creatures", locationNode)
    val items = getThings(folderPath, "items", locationNode)
    val other = getThings(folderPath, "other", locationNode)

    return Location(locationNode, activators, creatures, items, other, properties)
}

private fun getThings(folderPath: String, folderName: String, parent: LocationNode): NameSearchableList<Thing> {
    return getFiles(clean(folderPath, folderName)).map { core.thing.loadFromDisk(it.path, parent.network) }.toNameSearchableList()
}
