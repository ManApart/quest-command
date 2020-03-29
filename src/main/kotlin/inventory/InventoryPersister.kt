package inventory

import core.utility.NameSearchableList

fun persist(dataObject: Inventory, path: String) {
    dataObject.getItems().map { core.target.persist(it, path) }
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Inventory {
    val items = (data["items"] as List<Map<String, Any>>).map { core.target.readFromData(it) }
    return Inventory(listOf(), NameSearchableList(items))
}