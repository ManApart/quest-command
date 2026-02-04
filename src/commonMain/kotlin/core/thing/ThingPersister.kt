package core.thing

import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.MindP
import core.body.Body
import core.body.BodyManager
import core.body.NONE
import core.body.Slot
import core.properties.PropertiesP
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import status.SoulP
import system.mapper
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadFromPath
import system.persistance.writeSave
import traveling.location.Network
import traveling.location.location.LocationManager
import traveling.location.location.location

suspend fun persistToDisk(dataObject: Thing, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val data = mapper.encodeToString(ThingP(dataObject))
    //Persist Position
    writeSave(path, saveName, data)

//    inventory.persist(dataObject.inventory, clean(prefix, "inventory"))
    core.body.persist(dataObject.body, prefix)
}


suspend fun loadFromDisk(path: String, parentLocation: Network? = null): Thing {
    val json: ThingP = loadFromPath(path)!!
    return json.parsed(path, parentLocation)
}


@kotlinx.serialization.Serializable
data class ThingP(
    val name: String,
    val mind: MindP = MindP(),
    val behaviorRecipes: List<BehaviorRecipe> = emptyList(),
    val equipSlots: List<List<String>> = emptyList(),
    val description: String,
    val networkName: String,
    val locationName: String,
    val soul: SoulP = SoulP(),
    val properties: PropertiesP = PropertiesP(),
    //TODO Persist Position
    val body: String = NONE.name,
    @kotlinx.serialization.Transient
    private val bodyReference: Body? = null
){
    internal constructor(b: Thing): this(b.name, MindP(b.mind), b.behaviors.map { BehaviorRecipe(it.name, it.params) }, b.equipSlots.map { it.attachPoints }, b.description, b.location.network.name, b.location.name, SoulP(b.soul), PropertiesP(b.properties), b.body.name, b.body)

    suspend fun parsed(path: String, parentLocation: Network? = null): Thing {
        val folderPath = path.removeSuffix(".json")

        val location = parentLocation?.getLocationNodeOrNull(locationName)
            ?: LocationManager.getNetworkOrNull(networkName)?.getLocationNodeOrNull(locationName)
            ?: BodyManager.getBody(networkName).layout.getLocationNode(locationName)
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

    suspend fun persistReferences(path: String) {
        core.body.persist(bodyReference!!, path)
    }
}

suspend fun saveBody(thing: Thing, path: String){
    val prefix = clean(path, thing.name)
    core.body.persist(thing.body, prefix)
}
