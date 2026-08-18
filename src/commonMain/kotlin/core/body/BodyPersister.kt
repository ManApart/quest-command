package core.body

import core.utility.toNameSearchableList
import crafting.material.MaterialManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import system.mapper
import system.persistance.*
import traveling.position.NO_VECTOR
import traveling.position.Vector

//TODO - persist equipped items
suspend fun persist(dataObject: Body2, path: String) {
    //If Body is none or not custom, don't persist
    if (dataObject.name == NONE.name) return
    if (BodyManager.getBody2(dataObject.name) == dataObject) return

    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val json = mapper.encodeToString(BodyP(dataObject))
    writeSave(path, saveName, json)
}


suspend fun load(path: String, name: String): Body2 {
    if (name == NONE.name) return NONE

    val filePath = cleanPathToFile(".json", path, name)
    val json: BodyP? = loadFromPath(filePath)
    return json?.parsed() ?: BodyManager.getBody2(name)
}

@Serializable
data class BodyP(
    val name: String,
    val dimensions: Vector = NO_VECTOR,
    val parts: List<BodyPartP> = emptyList(),
) {
    constructor(b: Body2) : this(b.name, b.getDimensionsUnscaled(), b.parts.map { BodyPartP(it) })

    fun parsed(): Body2 {
        return Body2(name, dimensions, parts.map { it.parsed() }.toNameSearchableList())
    }
}

@Serializable
data class BodyPartP(val name: String, val material: String) {
    constructor(p: BodyPart) : this(p.name, p.material.name)
    fun parsed() = BodyPart(name, MaterialManager.getMaterial(material))
}
