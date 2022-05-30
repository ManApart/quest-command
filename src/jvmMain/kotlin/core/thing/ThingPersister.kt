package core.thing

import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.MindP
import core.body.Body
import core.body.Slot
import core.properties.Properties
import core.properties.PropertiesP
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import status.SoulP
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadFromPath
import system.persistance.writeSave
import traveling.location.Network
import traveling.location.location.LocationManager

fun persistToDisk(dataObject: Thing, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val data = Json.encodeToString(ThingP(dataObject))
    //Persist Position
    writeSave(path, saveName, data)

//    inventory.persist(dataObject.inventory, clean(prefix, "inventory"))
    core.body.persist(dataObject.body, prefix)
}


fun loadFromDisk(path: String, parentLocation: Network? = null): Thing {
    val json: ThingP = loadFromPath(path)!!
    return json.parsed(path, parentLocation)
}


@kotlinx.serialization.Serializable
data class ThingP(
    val name: String,
    val mind: MindP,
    val behaviorRecipes: List<BehaviorRecipe>,
    val equipSlots: List<List<String>>,
    val description: String,
    val networkName: String,
    val locationName: String,
    val soul: SoulP,
    val properties: PropertiesP,
    //TODO Persist Position
    val body: String,
    @kotlinx.serialization.Transient
    private val bodyReference: Body? = null
){
    internal constructor(b: Thing): this(b.name, MindP(b.mind), b.behaviors.map { BehaviorRecipe(it.name, it.params) }, b.equipSlots.map { it.attachPoints }, b.description, b.location.network.name, b.location.name, SoulP(b.soul), PropertiesP(b.properties), b.body.name, b.body)

    fun parsed(path: String, parentLocation: Network? = null): Thing {
        val folderPath = path.removeSuffix(".json")

        val network = parentLocation ?: LocationManager.getNetwork(networkName)
        val location = network.getLocationNode(locationName)
        val body = core.body.load(folderPath, body)

        return thing(name) {
            param(mutableMapOf<String, String>())
            mind(mind)
            behavior(behaviorRecipes)
            body(body)
            description(description)
            soul(soul.parsed(body))
            equipSlotOptions(equipSlots.map { Slot(it) })
            props(properties.parsed())
        }.build().also {
            it.location = location
        }
    }

    fun persistReferences(path: String) {
        core.body.persist(bodyReference!!, path)
    }
}

fun saveBody(thing: Thing, path: String){
    val prefix = clean(path, thing.name)
    core.body.persist(thing.body, prefix)
}