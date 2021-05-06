package core.body

import system.persistance.*

fun persist(dataObject: Body, path: String) {
    if (dataObject.name == NONE.name) {
        return
    }
    val prefix = clean(path, dataObject.name)
    traveling.location.persist(dataObject.layout, path, dataObject.getEquippedItems())
    dataObject.getEquippedItems().forEach { core.target.persist(it, path) }

    val saveName = cleanPathToFile("json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["slots"] = dataObject.getSlotMap()
    writeSave(path, saveName, data)
}

@Suppress("UNCHECKED_CAST")
fun load(path: String, name: String): Body {
    if (name == NONE.name) {
        return NONE
    }
    val filePath = cleanPathToFile(".json", path, name)
    val networkFolderPath = clean(path, name)

    val network = traveling.location.load(networkFolderPath, name)
    val data = loadMap(filePath)
    val slotMap = (data["slots"] as Map<String, String>).toMutableMap()

    val equippedItems = getFiles(path, listOf(filePath)).map { core.target.load(it.path, network) }

    val body = Body(name, network, slotMap)

    equippedItems.forEach {
        val slotName = slotMap[it.name]
        val slot = it.equipSlots.firstOrNull { equipSlot -> equipSlot.description == slotName }
        if (slot != null) {
            body.equip(it, slot)
        } else {
            body.equip(it)
        }
    }

    return body
}