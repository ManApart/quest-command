package core.target

import core.ai.behavior.getPersisted
import core.properties.getPersisted
import status.ProtoSoul
import system.persistance.clean
import system.persistance.writeSave
import traveling.location.location.DEFAULT_NETWORK
import traveling.location.location.LocationManager
import traveling.location.location.NOWHERE_NODE


fun persist(dataObject: Target, path: String) {
    val prefix = path + clean(dataObject.name)
    val saveName = "$prefix.json"
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["aiName"] = dataObject.ai.name
    data["behaviorRecipes"] = dataObject.behaviorRecipes.map { getPersisted(it) }
    data["equipSlots"] = dataObject.equipSlots.map { it.attachPoints }
    data["description"] = dialogue.getPersisted(dataObject.getDescriptionWithConditions())
    data["location"] = mapOf("network" to dataObject.location.network.name, "node" to dataObject.location.name)
    data["soul"] = status.getPersisted(dataObject.soul)
    data["properties"] = getPersisted(dataObject.properties)
    //Persist Position
    writeSave(path, saveName, data)

    //Only if inventory not empty?
    inventory.persist(dataObject.inventory, "$prefix/inventory/")
    core.body.persist(dataObject.body, "$prefix/body/")
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Target {
    val name = data["name"] as String
    val aiName = data["aiName"] as String
    val behaviorRecipes = (data["behaviorRecipes"] as List<Map<String, Any>>).map { core.ai.behavior.readFromData(it) }.toMutableList()
    val equipSlots = (data["equipSlots"] as List<List<String>>)
    val dynamicDescription = dialogue.readFromData(data["description"] as Map<String, Any>)
    //Instead of persisting inventory, read it from child locations? what about equipped items?
    //Persist list of equipped item names, re-equip items on load
    val inventory = inventory.readFromData(data["inventory"] as Map<String, Any>)
    val body = core.body.readFromData(data["body"] as Map<String, Any>, inventory)
    val locationMap = (data["location"] as Map<String, String>)
    val location = LocationManager.getNetwork(locationMap["network"] ?: DEFAULT_NETWORK.name).getLocationNode(locationMap["node"] ?: NOWHERE_NODE.name)
    val props = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val target = Target(name, null, mapOf(), null, aiName, behaviorRecipes, body, null, equipSlots, dynamicDescription, listOf(), location, null, ProtoSoul(), props)
    target.inventory.addAll(inventory.getAllItems())
    status.readFromData(data["soul"] as Map<String, Any>, target.soul)

    return target
}