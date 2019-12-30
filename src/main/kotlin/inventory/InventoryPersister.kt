package inventory

import core.target.getPersisted
import core.utility.NameSearchableList

fun getPersisted(dataObject: Inventory): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["items"] = dataObject.getItems().map { getPersisted(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Inventory {
    val items = (data["items"] as List<Map<String, Any>>).map { core.target.readFromData(it) }
    return Inventory(listOf(), NameSearchableList(items))
}