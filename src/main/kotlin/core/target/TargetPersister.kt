package core.target

import core.ai.behavior.BehaviorRecipe
import core.properties.getPersisted
import dialogue.DialogueOptions
import status.ProtoSoul
import traveling.location.DEFAULT_NETWORK
import traveling.location.LocationManager
import traveling.location.NOWHERE_NODE


fun getPersisted(dataObject: Target): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["aiName"] = dataObject.ai.name
    data["behaviorRecipes"] = dataObject.behaviorRecipes.map { it.name }
    //TODO - body persister for body details
    data["body"] = dataObject.body.name
    data["equipSlots"] = dataObject.equipSlots.map { it.attachPoints }
    //TODO - persister for dynamic dialogue
    data["description"] = dataObject.description
//    data["description"] = dataObject.dynamicDescription.getDialogue()
    //TODO - persister for items
    data["inventory"] = inventory.getPersisted(dataObject.inventory)
    data["location"] = mapOf("network" to dataObject.location.network.name, "node" to dataObject.location.name)
    data["soul"] = status.getPersisted(dataObject.soul)

    data["properties"] = getPersisted(dataObject.properties)
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Target {
    val name = data["name"] as String
    val aiName = data["aiName"] as String
    val behaviorRecipes = (data["behaviorRecipes"] as List<String>).map { BehaviorRecipe(it) }.toMutableList()
    val body = data["body"] as String
    val equipSlots = (data["equipSlots"] as List<List<String>>)
    val dynamicDescription = DialogueOptions(data["description"] as String)
    val inventory = inventory.readFromData(data["inventory"] as Map<String, Any>)
    val locationMap = (data["location"] as Map<String, String>)
    val location = LocationManager.getNetwork(locationMap["network"] ?: DEFAULT_NETWORK).getLocationNode(locationMap["node"] ?: NOWHERE_NODE.name)
    val props = core.properties.readFromData(data["properties"] as Map<String, Any>)

    val target = Target(name, null, mapOf(), null, aiName, behaviorRecipes, body, equipSlots, dynamicDescription, listOf(), location, null, ProtoSoul(), props)

    target.inventory.addAll(inventory.getAllItems())
    status.readFromData(data["soul"] as Map<String, Any>, target.soul)

    return target
}
