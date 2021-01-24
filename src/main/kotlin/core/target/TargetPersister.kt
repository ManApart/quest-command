package core.target

import core.ai.behavior.getPersisted
import core.conditional.getPersisted
import core.conditional.readFromData
import core.properties.getPersisted
import status.ProtoSoul
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadMap
import system.persistance.writeSave
import traveling.location.Network
import traveling.location.location.DEFAULT_NETWORK
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import traveling.location.location.NOWHERE_NODE


fun persist(dataObject: Target, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["aiName"] = dataObject.ai.name
    data["behaviorRecipes"] = dataObject.behaviorRecipes.map { getPersisted(it) }
    data["equipSlots"] = dataObject.equipSlots.map { it.attachPoints }
    data["description"] = getPersisted(dataObject.getDescriptionWithOptions())
    data["location"] = mapOf("network" to dataObject.location.network.name, "node" to dataObject.location.name)
    data["soul"] = status.getPersisted(dataObject.soul)
    data["properties"] = getPersisted(dataObject.properties)
    data["body"] = dataObject.body.name
    //Persist Position
    writeSave(path, saveName, data)

//    inventory.persist(dataObject.inventory, clean(prefix, "inventory"))
    core.body.persist(dataObject.body, prefix)

}

@Suppress("UNCHECKED_CAST")
fun load(path: String, parentLocation: Network? = null): Target {
    val params = mutableMapOf<String, String>()
    val folderPath = path.removeSuffix(".json")
    val data = loadMap(path)
    val name = data["name"] as String
    val aiName = data["aiName"] as String
    val behaviors = (data["behaviorRecipes"] as List<Map<String, Any>>).map { core.ai.behavior.readFromData(it) }.toMutableList()
    val equipSlots = (data["equipSlots"] as List<List<String>>)
    val dynamicDescription = readFromData(data["description"] as Map<String, Any>)
    val location = getLocation(parentLocation, data)
    val props = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val bodyName = data["body"] as String
    val body = core.body.load(folderPath, bodyName)

    val target = Target(name, null, params, null, aiName, behaviors, body, null, equipSlots, dynamicDescription, listOf(), location, null, ProtoSoul(), props)
    status.readFromData(data["soul"] as Map<String, Any>, target.soul)

    return target
}

@Suppress("UNCHECKED_CAST")
private fun getLocation(parentLocation: Network?, data: Map<String, Any>): LocationNode {
    val locationMap = (data["location"] as Map<String, String>)
    val network = parentLocation ?: LocationManager.getNetwork(locationMap["network"] ?: DEFAULT_NETWORK.name)
    return network.getLocationNode(locationMap["node"] ?: NOWHERE_NODE.name)
}