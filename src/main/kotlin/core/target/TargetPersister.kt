package core.target

import core.ai.behavior.BehaviorRecipe
import core.ai.behavior.getPersisted
import core.body.Slot
import core.properties.getPersisted
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadMap
import system.persistance.writeSave
import traveling.location.Network
import traveling.location.location.LocationManager
import traveling.location.network.DEFAULT_NETWORK
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE


fun persist(dataObject: Target, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["aiName"] = dataObject.ai.name
    val behaviorRecipes = dataObject.behaviors.map { BehaviorRecipe(it.name, it.params) }
    data["behaviorRecipes"] = behaviorRecipes.map { getPersisted(it) }
    data["equipSlots"] = dataObject.equipSlots.map { it.attachPoints }
    data["description"] = dataObject.description
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
    val behaviors = (data["behaviorRecipes"] as List<Map<String, Any>>).map { core.ai.behavior.readFromData(it) }
    val equipSlots = (data["equipSlots"] as List<List<String>>).map { Slot(it) }
    val description = data["description"] as String
    val location = getLocation(parentLocation, data)
    val props = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val bodyName = data["body"] as String
    val body = core.body.load(folderPath, bodyName)
    val soul = status.readFromData(data["soul"] as Map<String, Any>, body)

    return target(name) {
        param(params)
        ai(aiName)
        behavior(behaviors)
        body(body)
        description(description)
        soul(soul)
        equipSlotOptions(equipSlots)
        props(props)
    }.build().also {
        it.location = location
    }
}

@Suppress("UNCHECKED_CAST")
private fun getLocation(parentLocation: Network?, data: Map<String, Any>): LocationNode {
    val locationMap = (data["location"] as Map<String, String>)
    val network = parentLocation ?: LocationManager.getNetwork(locationMap["network"] ?: DEFAULT_NETWORK.name)
    return network.getLocationNode(locationMap["node"] ?: NOWHERE_NODE.name)
}