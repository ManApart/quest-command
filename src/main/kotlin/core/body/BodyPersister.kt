package core.body

import core.target.Target
import system.persistance.*

fun persist(dataObject: Body, path: String) {
    if (dataObject.name == NONE.name) {
        return
    }
    val prefix = clean(path, dataObject.name)
    traveling.location.persist(dataObject.layout, prefix, dataObject.getEquippedItems())
    dataObject.getEquippedItems().forEach { core.target.persist(it, path) }

    val saveName = cleanPathToFile("json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["slots"] = dataObject.getSlotMap()
    writeSave(path, saveName, data)
}

@Suppress("UNCHECKED_CAST")
fun load(path: String, name: String): Body {
    val folderPath = path.removeSuffix(".json")

    val network = traveling.location.load(clean(path, name), name)
    val data = loadMap(path)
    val slotMap = (data["slots"] as Map<String, String>).toMutableMap()

    val equippedItems = getFiles(folderPath).map { core.target.load(it.path) }

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