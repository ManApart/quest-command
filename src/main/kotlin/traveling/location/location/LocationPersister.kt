package traveling.location.location

import core.properties.getPersisted
import core.utility.toNameSearchableList
import system.persistance.clean
import system.persistance.getFiles
import system.persistance.loadMap
import system.persistance.writeSave
import traveling.location.Network

fun persist(dataObject: Location, path: String){
    val prefix = path + clean(dataObject.name)
    val saveName = "$prefix.json"
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.locationNode.name
    data["properties"] = getPersisted(dataObject.properties)
    writeSave(path, saveName, data)

    data["activators"] = dataObject.getActivators().map { core.target.persist(it, "$prefix/activators/") }
    data["creatures"] = dataObject.getCreatures().filter { !it.isPlayer() }.map { core.target.persist(it, "$prefix/creatures/") }
    data["items"] = dataObject.getItems().map { core.target.persist(it, "$prefix/items/") }
    data["other"] = dataObject.getOther().map { core.target.persist(it, "$prefix/other/") }
    //Persist weather
    //Persist last weather change

}

@Suppress("UNCHECKED_CAST")
fun load(path: String, locationNode: LocationNode): Location {
    val data = loadMap(path)
    val name = data["name"] as String
    val prefix = path + clean(name)
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val activators = getFiles("$prefix/activators/").map { core.target.load(it.path) }.toNameSearchableList()
    val creatures = getFiles("$prefix/creatures/").map { core.target.load(it.path) }.toNameSearchableList()
    val items = getFiles("$prefix/items/").map { core.target.load(it.path) }.toNameSearchableList()
    val other = getFiles("$prefix/other/").map { core.target.load(it.path) }.toNameSearchableList()

    return Location(locationNode, activators, creatures, items, other, properties)
}