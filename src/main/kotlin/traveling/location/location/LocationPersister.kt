package traveling.location.location

import core.properties.getPersisted
import core.utility.toNameSearchableList
import system.persistance.clean
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
    data["creatures"] = dataObject.getCreatures().map { core.target.persist(it, "$prefix/creatures/") }
    data["items"] = dataObject.getItems().map { core.target.persist(it, "$prefix/items/") }
    data["other"] = dataObject.getOther().map { core.target.persist(it, "$prefix/other/") }
    //Persist weather
    //Persist last weather change

}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, network: Network): Location {
    val name = data["name"] as String
    val locationNode = network.getLocationNode(name)
    val properties = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val activators = (data["activators"] as List<Map<String, Any>>).map { core.target.readFromData(it) }.toNameSearchableList()
    val creatures = (data["creatures"] as List<Map<String, Any>>).map { core.target.readFromData(it) }.toNameSearchableList()
    val items = (data["items"] as List<Map<String, Any>>).map { core.target.readFromData(it) }.toNameSearchableList()
    val other = (data["other"] as List<Map<String, Any>>).map { core.target.readFromData(it) }.toNameSearchableList()
    return Location(locationNode, activators, creatures, items, other, properties)
}