package core.body

import crafting.material.MaterialManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import system.persistance.*

suspend fun persist(dataObject: Body, path: String) {
    if (dataObject.name == NONE.name) {
        return
    }
    val prefix = clean(path, dataObject.name)
    traveling.location.persist(dataObject.layout, path, dataObject.getEquippedItems())
    dataObject.getEquippedItems().forEach { core.thing.persistToDisk(it, path) }

    val saveName = cleanPathToFile("json", prefix)
    val json = Json.encodeToString(BodyP(dataObject))
    writeSave(path, saveName, json)
}


suspend fun load(path: String, name: String): Body {
    if (name == NONE.name) {
        return NONE
    }
    val filePath = cleanPathToFile(".json", path, name)
    val json: BodyP = loadFromPath(filePath)!!
    return json.parsed(path)
}

@kotlinx.serialization.Serializable
data class BodyP(
    val name: String,
    val material: String,
    val slots: Map<String, String>,
) {
    constructor(b: Body) : this(b.name, b.material.name, b.getSlotMap())

    suspend fun parsed(path: String): Body {
        val filePath = cleanPathToFile(".json", path, name)
        val networkFolderPath = clean(path, name)
        val network = traveling.location.load(networkFolderPath, name)
        val equippedItems = getFiles(path, listOf(filePath)).map { core.thing.loadFromDisk(it.path, network) }
        return Body(name, MaterialManager.getMaterial(material), network, slots.toMutableMap()).apply {
            equipItems(equippedItems)
        }
    }
}