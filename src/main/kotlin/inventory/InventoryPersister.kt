package inventory

import core.utility.NameSearchableList
import system.persistance.getFiles
import traveling.location.Network

fun persist(dataObject: Inventory, path: String) {
    dataObject.getItems().map { core.target.persist(it, path) }
}

@Suppress("UNCHECKED_CAST")
fun load(path: String): Inventory {
    val items = getFiles(path).map { core.target.load(it.path) }
    return Inventory(listOf(), NameSearchableList(items))
}