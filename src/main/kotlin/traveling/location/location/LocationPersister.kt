package traveling.location.location

import core.properties.getPersisted
import core.utility.toNameSearchableList
import inventory.Inventory
import traveling.location.Network

fun getPersisted(dataObject: Location): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.locationNode.name
    data["activators"] = dataObject.getActivators().map { core.target.getPersisted(it) }
    data["creatures"] = dataObject.getCreatures().map { core.target.getPersisted(it) }
    data["items"] = dataObject.getItems().map { core.target.getPersisted(it) }
    data["other"] = dataObject.getOther().map { core.target.getPersisted(it) }
    data["properties"] = getPersisted(dataObject.properties)
    //Persist weather
    //Persist last weather change
    return data
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