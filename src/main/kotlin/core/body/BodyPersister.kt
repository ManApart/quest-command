package core.body

import inventory.Inventory
import system.persistance.clean
import system.persistance.writeSave

fun persist(dataObject: Body, path: String) {
    if (dataObject.name == NONE.name) {
        return
    }
    val prefix = path + clean(dataObject.name)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    writeSave(path, "$prefix.json", data)

    traveling.location.persist(dataObject.layout, prefix)
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, inventory: Inventory?): Body {
    val name = data["name"] as String
    val network = traveling.location.readFromData(data["layout"] as Map<String, Any>, inventory)
    return Body(name, network)
}