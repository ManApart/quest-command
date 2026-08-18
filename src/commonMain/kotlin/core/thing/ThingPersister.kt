package core.thing

import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.MindP
import core.properties.PropertiesP
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import status.SoulP
import system.mapper
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadFromPath
import system.persistance.writeSave
import traveling.location.Network
import traveling.location.location.LocationManager
import traveling.position.Vector

//TODO - persist inventory
suspend fun persistToDisk(dataObject: Thing, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val data = mapper.encodeToString(ThingP(dataObject))
    writeSave(path, saveName, data)

//    inventory.persist(dataObject.inventory, clean(prefix, "inventory"))
    core.body.persist(dataObject.body2, prefix)
}

suspend fun loadFromDisk(path: String, parentLocation: Network? = null): Thing {
    val json: ThingP = loadFromPath(path)!!
    return json.parsed(path, parentLocation)
}


@Serializable
data class ThingP(
    val name: String,
    val mind: MindP = MindP(),
    val bodyName: String,
    val behaviorRecipes: List<BehaviorRecipe> = emptyList(),
    val description: String,
    val networkName: String,
    val locationName: String,
    val position: Vector,
    val soul: SoulP = SoulP(),
    val properties: PropertiesP = PropertiesP(),
) {
    internal constructor(b: Thing) : this(
        b.name,
        MindP(b.mind),
        b.body2.name,
        b.behaviors.map { BehaviorRecipe(it.name, it.params) },
        b.description,
        b.location.network.name,
        b.location.name,
        b.position,
        SoulP(b.soul),
        PropertiesP(b.properties)
    )

    suspend fun parsed(path: String, parentLocation: Network? = null): Thing {
        val folderPath = path.removeSuffix(".json")

        val location = parentLocation?.getLocationNodeOrNull(locationName)
            ?: LocationManager.getNetwork(networkName).getLocationNode(locationName)
        val body = core.body.load(folderPath, bodyName)

        return thing(name) {
            param(mutableMapOf<String, String>())
            mind(mind)
            behavior(behaviorRecipes)
            body(body)
            position(position)
            description(description)
            soul(soul.parsed(body))
            props(properties.parsed())
        }.build().also {
            it.location = location
        }
    }
}
