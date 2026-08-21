package core.body

import core.utility.toNameSearchableList
import crafting.material.MaterialManager
import inventory.Inventory
import inventory.ViewEquipped
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import system.mapper
import system.persistance.*
import traveling.position.NO_VECTOR
import traveling.position.Vector

suspend fun persist(dataObject: Body, path: String) {
    //If Body is none, don't persist
    if (dataObject.name == NONE.name) return

    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val json = mapper.encodeToString(BodyP(dataObject))
    writeSave(path, saveName, json)
}

suspend fun load(path: String, name: String, inventory: Inventory): Body {
    if (name == NONE.name) return NONE

    val filePath = cleanPathToFile(".json", path, name)
    val json: BodyP? = loadFromPath(filePath)
    return json?.parsed(inventory) ?: BodyManager.getBody(name)
}

private typealias ItemName = String

@Serializable
data class BodyP(
    val name: String,
    val dimensions: Vector = NO_VECTOR,
    val parts: List<BodyPartP> = emptyList(),
    val equipped: Map<ItemName, EquipTarget> = emptyMap(),
) {
    constructor(b: Body) : this(b.name, b.getDimensionsUnscaled(), b.parts.map { BodyPartP(it) }, getEquipped(b))

    fun parsed(inventory: Inventory): Body {
        return Body(name, dimensions, parts.map { it.parsed() }.toNameSearchableList()).apply {
            equipped.forEach { (itemName, target) ->
                inventory.getItem(itemName)?.let { equip(it, target) }
            }
        }
    }
}

private fun getEquipped(b: Body) = b.equippedItems.values.associate { it.item.name to it.toEquipTarget() }

@Serializable
data class BodyPartP(val name: String, val material: String) {
    constructor(p: BodyPart) : this(p.name, p.material.name)
    fun parsed() = BodyPart(name, MaterialManager.getMaterial(material))
}
