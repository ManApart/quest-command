package traveling.location.location

import core.properties.getPersisted
import core.utility.toNameSearchableList
import system.persistance.*

fun persist(dataObject: Location, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile(".json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.locationNode.name
    data["properties"] = getPersisted(dataObject.properties)
    writeSave(path, saveName, data)

    data["activators"] = dataObject.getActivators().map { core.target.persist(it, clean(prefix, "activators")) }
    data["creatures"] = dataObject.getCreatures().filter { !it.isPlayer() }.map { core.target.persist(it, clean(prefix, "creatures")) }
    data["items"] = dataObject.getItems().map { core.target.persist(it, clean(prefix, "items")) }
    data["other"] = dataObject.getOther().map { core.target.persist(it, clean(prefix, "other")) }
    //Persist weather
    //Persist last weather change

}

@Suppress("UNCHECKED_CAST")
fun load(path: String, locationNode: LocationNode): Location {
    val data = loadMap(path)
    val folderPath = path.removeSuffix(".json")
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val activators = getFiles(clean(folderPath, "activators")).map { core.target.load(it.path) }.toNameSearchableList()
    val creatures = getFiles(clean(folderPath, "creatures")).map { core.target.load(it.path) }.toNameSearchableList()
    val items = getFiles(clean(folderPath, "items")).map { core.target.load(it.path) }.toNameSearchableList()
    val other = getFiles(clean(folderPath, "other")).map { core.target.load(it.path) }.toNameSearchableList()

    return Location(locationNode, activators, creatures, items, other, properties)
}